package cn.ghost.model.dto;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 17:53
 */
@Data
public class AppRuleDTO {
    private Integer id;

    private Integer appId;

    private String appName;

    private String version;

    private String matchObject;

    private String matchKey;

    private Byte matchMethod;

    private String matchRule;

    private Integer priority;
}
