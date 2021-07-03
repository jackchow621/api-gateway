package cn.ghost.service;

import cn.ghost.model.AppRuleDTO;
import cn.ghost.model.dto.ChangeStatusDTO;
import cn.ghost.model.dto.RuleDTO;
import cn.ghost.model.vo.RuleVO;

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
