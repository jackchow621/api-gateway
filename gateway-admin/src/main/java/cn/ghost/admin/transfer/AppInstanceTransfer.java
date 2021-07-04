package cn.ghost.admin.transfer;

import cn.ghost.admin.model.bean.AppInstance;
import cn.ghost.admin.model.vo.InstanceVO;
import cn.ghost.common.model.ServiceInstance;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AppInstanceTransfer {

    AppInstanceTransfer INSTANCE = Mappers.getMapper(AppInstanceTransfer.class);

    ServiceInstance mapToService(AppInstance appInstance);

    List<ServiceInstance> mapToServiceList(List<AppInstance> appInstances);

    @Mappings({
            @Mapping(target = "createdTime", expression = "java(cn.sp.utils.DateUtils.formatToYYYYMMDDHHmmss(appInstance.getCreatedTime()))")
    })
    InstanceVO mapToVO(AppInstance appInstance);

    List<InstanceVO> mapToVOS(List<AppInstance> appInstances);
}
