package cn.ghost.transfer;

import cn.ghost.model.AppRuleDTO;
import cn.ghost.model.bean.RouteRule;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 18:06
 */
@Mapper
public interface AppRuleVOTransfer {
    AppRuleVOTransfer INSTANCE = Mappers.getMapper(AppRuleVOTransfer.class);

    AppRuleDTO mapToVO(RouteRule routeRule);

    List<AppRuleDTO> mapToVOList(List<RouteRule> routeRules);
}
