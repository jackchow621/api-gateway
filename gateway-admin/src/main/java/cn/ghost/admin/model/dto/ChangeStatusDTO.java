package cn.ghost.admin.model.dto;

import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:54
 */
@Data
public class ChangeStatusDTO {
    private Integer id;

    private Byte enabled;

    private String appName;
}
