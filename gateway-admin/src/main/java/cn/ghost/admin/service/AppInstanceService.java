package cn.ghost.admin.service;

import cn.ghost.admin.model.dto.UpdateWeightDTO;
import cn.ghost.admin.model.vo.InstanceVO;

import java.util.List;

public interface AppInstanceService {

    List<InstanceVO> queryList(Integer appId);

    void updateWeight(UpdateWeightDTO updateWeightDTO);
}
