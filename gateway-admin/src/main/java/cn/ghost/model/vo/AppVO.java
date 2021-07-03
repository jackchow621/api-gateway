package cn.ghost.model.vo;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:28
 */
@Data
public class AppVO {
    private Integer id;

    private String appName;

    private String description;

    private String contextPath;

    private Integer instanceNum;

    private Byte enabled;

    private String createdTime;

}
