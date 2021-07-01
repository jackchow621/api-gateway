package cn.ghost.service;

import cn.ghost.model.AppInfoDTO;
import cn.ghost.model.RegisterAppDTO;
import cn.ghost.model.dto.ChangeStatusDTO;
import cn.ghost.model.vo.AppVO;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
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
