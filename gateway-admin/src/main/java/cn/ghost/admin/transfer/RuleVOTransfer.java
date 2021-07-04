package cn.ghost.admin.transfer;

import cn.ghost.admin.model.bean.RouteRule;
import cn.ghost.admin.model.vo.RuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RuleVOTransfer {
    RuleVOTransfer INSTANCE = Mappers.getMapper(RuleVOTransfer.class);

    @Mappings({
            @Mapping(target = "createdTime", expression = "java(cn.sp.utils.DateUtils.formatToYYYYMMDDHHmmss(rule.getCreatedTime()))")
    })
    RuleVO mapToVO(RouteRule rule);

    List<RuleVO> mapToVOList(List<RouteRule> rules);
}
