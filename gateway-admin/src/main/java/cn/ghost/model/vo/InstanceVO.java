package cn.ghost.model.vo;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
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
