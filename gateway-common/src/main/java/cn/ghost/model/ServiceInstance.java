package cn.ghost.model;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:15
 */
@Data
public class ServiceInstance {
    private String appName;

    private String ip;

    private Integer port;

    private String version;

    private Integer weight;
}
