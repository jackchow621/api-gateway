package cn.ghost.filter;

import cn.ghost.cache.ServiceCache;
import cn.ghost.chain.PluginChain;
import cn.ghost.config.ServerConfig;
import cn.ghost.constants.GatewayExceptionEnum;
import cn.ghost.exception.GatewayException;
import cn.ghost.plugin.AuthPlugin;
import cn.ghost.plugin.DynamicRoutePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.RequestPath;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:20
 */
public class PluginFilter implements WebFilter {

    @Autowired
    private ServerConfig properties;

    /*public PluginFilter(ServerConfig properties) {
        this.properties = properties;
    }*/

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String appName = parseAppName(serverWebExchange);
        if (CollectionUtils.isEmpty(ServiceCache.getAllInstances(appName))) {
            throw new GatewayException(GatewayExceptionEnum.SERVICE_NOT_FIND);
        }
        PluginChain pluginChain = new PluginChain(properties, appName);
        pluginChain.addPlugin(new AuthPlugin(properties));
        pluginChain.addPlugin(new DynamicRoutePlugin(properties));
        return pluginChain.execute(serverWebExchange, pluginChain);
    }

    private String parseAppName(ServerWebExchange exchange) {
        RequestPath path = exchange.getRequest().getPath();
        String appName = path.value().split("/")[1];
        return appName;
    }
}
