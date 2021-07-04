package cn.ghost.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/04 11:27
 */
@ConfigurationProperties(prefix = "gateway.http")
@Data
public class ClientConfig {
    private Integer port;
    private String contextPath;
    private String appName;

    private String version;

    private String adminUrl;
}
