package cn.ghost.admin.model.vo;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 14:44
 */
@Data
public class InstanceVO {
    private Integer id;

    private String appName;

    private String version;

    private String ip;

    private Integer port;

    private Integer weight;

    private String createdTime;
}
