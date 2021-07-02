package cn.ghost.service;

import cn.ghost.model.dto.UpdateWeightDTO;
import cn.ghost.model.vo.InstanceVO;

import java.util.List;

public interface AppInstanceService {

    List<InstanceVO> queryList(Integer appId);

    void updateWeight(UpdateWeightDTO updateWeightDTO);
}
