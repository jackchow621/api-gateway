package cn.ghost.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 14:45
 */
@Data
public class UpdateWeightDTO {
    @NotNull(message = "实例id不能为空")
    private Integer id;

    @NotNull(message = "权重不能为空")
    private Integer weight;
}
