package cn.ghost.chain;

import cn.ghost.cache.PluginCache;
import cn.ghost.config.ServerConfig;
import cn.ghost.plugin.AbstractGatewayPlugin;
import cn.ghost.plugin.GatewayPlugin;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:29
 */
@Getter
public class PluginChain extends AbstractGatewayPlugin {
    private int pos;

    private List<GatewayPlugin> plugins;

    private final String appName;

    public PluginChain(ServerConfig properties, String appName) {
        super(properties);
        this.appName = appName;
    }

    public void addPlugin(GatewayPlugin gatewayPlugin) {
        if (plugins == null) {
            plugins = Lists.newArrayList();
        }
        if (!PluginCache.isEnabled(appName, gatewayPlugin.name())) {
            return;
        }
        plugins.add(gatewayPlugin);
        // order by the plugin's order
        plugins.sort(Comparator.comparing(GatewayPlugin::order));
    }

    @Override
    public Integer order() {
        return pos;
    }

    @Override
    public String name() {
        return appName;
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, PluginChain pluginChain) {
        if (pos == plugins.size()) {
            return exchange.getResponse().setComplete();
        }
        return pluginChain.plugins.get(pos++).execute(exchange, pluginChain);
    }

}
