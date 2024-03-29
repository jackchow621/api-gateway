package cn.ghost.common.model.dto;

import cn.ghost.common.model.ServiceInstance;
import lombok.Data;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:14
 */
@Data
public class AppInfoDTO {
    private Integer appId;

    private String appName;

    private Byte enabled;

    private List<ServiceInstance> instances;

    private List<String> enabledPlugins;
}
