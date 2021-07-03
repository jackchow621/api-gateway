package cn.ghost.plugin;

import cn.ghost.chain.PluginChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:23
 */
public interface GatewayPlugin {
    Integer order();

    String name();

    Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain);
}
