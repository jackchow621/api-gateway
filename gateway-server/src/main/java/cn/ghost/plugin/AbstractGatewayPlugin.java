package cn.ghost.plugin;

import cn.ghost.config.ServerConfig;
import lombok.AllArgsConstructor;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:26
 */
@AllArgsConstructor
public abstract class AbstractGatewayPlugin implements GatewayPlugin {
    protected ServerConfig properties;
}
