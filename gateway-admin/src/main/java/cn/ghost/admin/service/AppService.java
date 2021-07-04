package cn.ghost.admin.service;

import cn.ghost.admin.model.dto.ChangeStatusDTO;
import cn.ghost.admin.model.vo.AppVO;
import cn.ghost.common.model.dto.AppInfoDTO;
import cn.ghost.common.model.dto.RegisterAppDTO;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:24
 */
public interface AppService {
    void register(RegisterAppDTO registerAppDTO);

    void unregister(RegisterAppDTO registerAppDTO);

    List<AppInfoDTO> getAppInfos(List<String> appNames);

    List<AppVO> getList();

    void updateEnabled(ChangeStatusDTO statusDTO);

    void delete(Integer id);
}
