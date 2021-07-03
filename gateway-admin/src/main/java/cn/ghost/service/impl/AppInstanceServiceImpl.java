package cn.ghost.service.impl;

import cn.ghost.mapper.AppInstanceMapper;
import cn.ghost.mapper.AppMapper;
import cn.ghost.model.bean.App;
import cn.ghost.model.bean.AppInstance;
import cn.ghost.model.dto.UpdateWeightDTO;
import cn.ghost.model.vo.InstanceVO;
import cn.ghost.service.AppInstanceService;
import cn.ghost.transfer.AppInstanceTransfer;
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
