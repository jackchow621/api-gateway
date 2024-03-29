package cn.ghost.admin.service.impl;

import cn.ghost.admin.mapper.AppInstanceMapper;
import cn.ghost.admin.mapper.AppMapper;
import cn.ghost.admin.model.bean.App;
import cn.ghost.admin.model.bean.AppInstance;
import cn.ghost.admin.model.dto.UpdateWeightDTO;
import cn.ghost.admin.model.vo.InstanceVO;
import cn.ghost.admin.service.AppInstanceService;
import cn.ghost.admin.transfer.AppInstanceTransfer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 14:45
 */
@Service
@Slf4j
public class AppInstanceServiceImpl implements AppInstanceService {
    @Resource
    private AppInstanceMapper appInstanceMapper;
    @Resource
    private AppMapper appMapper;

    @Override
    public List<InstanceVO> queryList(Integer appId) {
        App app = appMapper.selectById(appId);
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId, appId);
        List<AppInstance> instanceList = appInstanceMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(instanceList)) {
            return Lists.newArrayList();
        }
        List<InstanceVO> voList = AppInstanceTransfer.INSTANCE.mapToVOS(instanceList);
        voList.forEach(vo -> vo.setAppName(app.getAppName()));
        return voList;
    }

    @Override
    public void updateWeight(UpdateWeightDTO updateWeightDTO) {
        AppInstance appInstance = new AppInstance();
        appInstance.setId(updateWeightDTO.getId());
        appInstance.setWeight(updateWeightDTO.getWeight());
        appInstanceMapper.updateById(appInstance);
    }
}
