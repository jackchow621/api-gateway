package cn.ghost.model;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 19:20
 */
@Data
public class NacosMetadata {
    private String appName;

    private String version;

    private String plugins;
}
