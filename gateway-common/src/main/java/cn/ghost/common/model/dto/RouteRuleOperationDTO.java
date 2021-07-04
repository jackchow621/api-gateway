package cn.ghost.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRuleOperationDTO {
    private String operationType;

    private List<AppRuleDTO> ruleList;

}
