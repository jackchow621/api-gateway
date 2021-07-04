package cn.ghost.admin.model.vo;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:51
 */
@Data
public class RuleVO {
    private Integer id;

    private String name;

    private Integer appId;

    private String appName;

    private String version;

    private String matchObject;

    private String matchKey;

    private Byte matchMethod;

    private String matchRule;

    private Byte enabled;

    private String createdTime;

    private Integer priority;

    private String matchStr;
}
