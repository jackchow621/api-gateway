package cn.ghost.service.impl;

import cn.ghost.model.AppInfoDTO;
import cn.ghost.model.RegisterAppDTO;
import cn.ghost.model.dto.ChangeStatusDTO;
import cn.ghost.model.vo.AppVO;
import cn.ghost.service.AppService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 19:28
 */
@Service
public class AppServiceImpl implements AppService {
    @Override
    public void register(RegisterAppDTO registerAppDTO) {

    }

    @Override
    public void unregister(RegisterAppDTO registerAppDTO) {

    }

    @Override
    public List<AppInfoDTO> getAppInfos(List<String> appNames) {
        return null;
    }

    @Override
    public List<AppVO> getList() {
        return null;
    }

    @Override
    public void updateEnabled(ChangeStatusDTO statusDTO) {

    }

    @Override
    public void delete(Integer id) {

    }
}
