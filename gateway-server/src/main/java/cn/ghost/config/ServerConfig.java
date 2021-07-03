package cn.ghost.config;

import cn.ghost.constants.GatewayExceptionEnum;
import cn.ghost.constants.LoadBalanceConstants;
import cn.ghost.exception.GatewayException;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:12
 */
@Component
@ConfigurationProperties(prefix = "gateway")
@Data
public class ServerConfig implements InitializingBean {

    /**
     * 负载均衡算法，默认轮询
     */
    private String loadBalance = LoadBalanceConstants.ROUND;
    /**
     * 网关超时时间，默认3s
     */
    private Long timeOutMillis = 3000L;
    /**
     * 缓存刷新间隔，默认10s
     */
    private Long cacheRefreshInterval = 10L;

    private String webSocketHostname;

    private Integer webSocketPort;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.webSocketPort == null || this.webSocketPort <= 0) {
            throw new GatewayException(GatewayExceptionEnum.CONFIG_ERROR);
        }
    }
}
