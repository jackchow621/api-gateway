package cn.ghost.admin.service;


import cn.ghost.admin.model.dto.ChangeStatusDTO;
import cn.ghost.admin.model.dto.RuleDTO;
import cn.ghost.admin.model.vo.RuleVO;
import cn.ghost.common.model.dto.AppRuleDTO;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:50
 */
public interface RuleService {
    List<AppRuleDTO> getEnabledRule();

    void add(RuleDTO ruleDTO);

    void delete(Integer id);

    List<RuleVO> queryList(String appName);

    void changeStatus(ChangeStatusDTO statusDTO);
}
