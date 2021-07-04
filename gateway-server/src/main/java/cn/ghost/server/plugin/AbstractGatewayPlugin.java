package cn.ghost.server.plugin;

import cn.ghost.server.config.ServerConfig;
import lombok.AllArgsConstructor;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:26
 */
@AllArgsConstructor
public abstract class AbstractGatewayPlugin implements cn.ghost.server.plugin.GatewayPlugin {
    protected ServerConfig properties;
}
