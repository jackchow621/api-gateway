package cn.ghost.server.plugin;

import cn.ghost.common.constants.GatewayPluginEnum;
import cn.ghost.server.chain.PluginChain;
import cn.ghost.server.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:38
 */
@Slf4j
public class AuthPlugin extends AbstractGatewayPlugin {
    public AuthPlugin(ServerConfig properties) {
        super(properties);
    }

    @Override
    public Integer order() {
        return GatewayPluginEnum.AUTH.getOrder();
    }

    @Override
    public String name() {
        return GatewayPluginEnum.AUTH.getName();
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        log.info("auth plugin...");
        return pluginChain.execute(exchange, pluginChain);
    }
}
