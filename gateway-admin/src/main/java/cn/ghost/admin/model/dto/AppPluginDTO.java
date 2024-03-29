package cn.ghost.admin.model.dto;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 11:46
 */
@Data
public class AppPluginDTO {
    private Integer appId;

    private String appName;

    private String code;
}
