package cn.ghost.common.model.dto;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 11:34
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
