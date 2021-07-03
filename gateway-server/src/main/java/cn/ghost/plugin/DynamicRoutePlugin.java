package cn.ghost.plugin;

import cn.ghost.cache.RouteRuleCache;
import cn.ghost.cache.ServiceCache;
import cn.ghost.chain.PluginChain;
import cn.ghost.config.ServerConfig;
import cn.ghost.constants.GatewayExceptionEnum;
import cn.ghost.constants.GatewayPluginEnum;
import cn.ghost.constants.MatchObjectEnum;
import cn.ghost.exception.GatewayException;
import cn.ghost.model.AppRuleDTO;
import cn.ghost.model.Result;
import cn.ghost.model.ServiceInstance;
import cn.ghost.spi.loadbalance.LoadBalance;
import cn.ghost.spi.loadbalance.LoadBalanceFactory;
import cn.ghost.utils.StringTools;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:42
 */
@Slf4j
public class DynamicRoutePlugin extends AbstractGatewayPlugin {

    private static final Gson gson = new GsonBuilder().create();

    public DynamicRoutePlugin(ServerConfig properties) {
        super(properties);
    }

    @Override
    public Integer order() {
        return GatewayPluginEnum.DYNAMIC_ROUTE.getOrder();
    }

    @Override
    public String name() {
        return GatewayPluginEnum.DYNAMIC_ROUTE.getName();
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        String appName = pluginChain.getAppName();
        ServiceInstance serviceInstance = chooseInstance(appName, exchange.getRequest());
        log.info("selected instance is [{}]", gson.toJson(serviceInstance));
        // request service
        String url = buildUrl(exchange, serviceInstance);
        return forward(exchange, url);
    }

    private ServiceInstance chooseInstance(String appName, ServerHttpRequest request) {
        List<ServiceInstance> serviceInstances = ServiceCache.getAllInstances(appName);
        if (CollectionUtils.isEmpty(serviceInstances)) {
            log.error("service instance of {} not find", appName);
            throw new GatewayException(GatewayExceptionEnum.SERVICE_NOT_FIND);
        }
        String version = matchAppVersion(appName, request);
        if (StringUtils.isEmpty(version)) {
            throw new GatewayException("match app version error");
        }
        // filter serviceInstances by version
        List<ServiceInstance> instances = serviceInstances.stream().filter(i -> i.getVersion().equals(version)).collect(Collectors.toList());
        //Select an instance based on the load balancing algorithm
        LoadBalance loadBalance = LoadBalanceFactory.getInstance(properties.getLoadBalance(), appName, version);
        ServiceInstance serviceInstance = loadBalance.chooseOne(instances);
        return serviceInstance;
    }

    private String matchAppVersion(String appName, ServerHttpRequest request) {
        List<AppRuleDTO> rules = RouteRuleCache.getRules(appName);
        rules.sort(Comparator.comparing(AppRuleDTO::getPriority).reversed());
        for (AppRuleDTO rule : rules) {
            if (match(rule, request)) {
                return rule.getVersion();
            }
        }
        return null;
    }

    private boolean match(AppRuleDTO rule, ServerHttpRequest request) {
        String matchObject = rule.getMatchObject();
        String matchKey = rule.getMatchKey();
        String matchRule = rule.getMatchRule();
        Byte matchMethod = rule.getMatchMethod();
        if (MatchObjectEnum.DEFAULT.getCode().equals(matchObject)) {
            return true;
        } else if (MatchObjectEnum.QUERY.getCode().equals(matchObject)) {
            String param = request.getQueryParams().getFirst(matchKey);
            if (!StringUtils.isEmpty(param)) {
                return StringTools.match(param, matchMethod, matchRule);
            }
        } else if (MatchObjectEnum.HEADER.getCode().equals(matchObject)) {
            HttpHeaders headers = request.getHeaders();
            String headerValue = headers.getFirst(matchKey);
            if (!StringUtils.isEmpty(headerValue)) {
                return StringTools.match(headerValue, matchMethod, matchRule);
            }
        }
        return false;
    }

    private String buildUrl(ServerWebExchange exchange, ServiceInstance serviceInstance) {
        ServerHttpRequest request = exchange.getRequest();
        String query = request.getURI().getQuery();
        String path = request.getPath().value().replaceFirst("/" + serviceInstance.getAppName(), "");
        String url = "http://" + serviceInstance.getIp() + ":" + serviceInstance.getPort() + path;
        if (!StringUtils.isEmpty(query)) {
            url = url + "?" + query;
        }
        return url;
    }

    private Mono<Void> forward(ServerWebExchange exchange, String url) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpMethod method = request.getMethod();

        WebClient.RequestBodySpec requestBodySpec = null;
//        webClient.method(method).uri(url)
//                .headers((headers) -> headers.addAll(request.getHeaders()));

        WebClient.RequestHeadersSpec<?> reqHeadersSpec;
        if (requireHttpBody(method)) {
            reqHeadersSpec = requestBodySpec.body(BodyInserters.fromDataBuffers(request.getBody()));
        } else {
            reqHeadersSpec = requestBodySpec;
        }
        // nio->callback->nio
        return reqHeadersSpec.exchange().timeout(Duration.ofMillis(properties.getTimeOutMillis()))
                .onErrorResume(ex -> Mono.defer(() -> {
                    Result result;
                    if (ex instanceof TimeoutException) {
                        result = Result.fail(GatewayExceptionEnum.NETWORK_TIMEOUT_ERROR);
//                        errorResultJson = "{\"code\":5001,\"message\":\"network timeout\"}";
                    } else {
                        result = Result.fail(GatewayExceptionEnum.SYSTEM_ERROR);
//                        errorResultJson = "{\"code\":5000,\"message\":\"system error\"}";
                    }
                    return doResponse(exchange, JSON.toJSONString(result));
                }).then(Mono.empty())).flatMap(backendResponse -> {
                    response.setStatusCode(backendResponse.statusCode());
                    response.getHeaders().putAll(backendResponse.headers().asHttpHeaders());
                    return response.writeWith(backendResponse.bodyToFlux(DataBuffer.class));
                });
    }

    public static Mono<Void> doResponse(ServerWebExchange exchange, String resp) {
        Assert.notNull(resp, "response object can't be null");
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(resp.getBytes())));
    }

    private boolean requireHttpBody(HttpMethod method) {
        return (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.PATCH));
    }
}
