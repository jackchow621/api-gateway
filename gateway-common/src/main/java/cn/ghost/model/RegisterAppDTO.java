package cn.ghost.model;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:25
 */
@Data
public class RegisterAppDTO {
    private String appName;

    private String contextPath;

    private String version;

    private String ip;

    private Integer port;
}
