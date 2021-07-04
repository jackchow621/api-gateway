package cn.ghost.admin.service.impl;

import cn.ghost.admin.mapper.AppInstanceMapper;
import cn.ghost.admin.mapper.AppMapper;
import cn.ghost.admin.mapper.AppPluginMapper;
import cn.ghost.admin.mapper.PluginMapper;
import cn.ghost.admin.model.bean.App;
import cn.ghost.admin.model.bean.AppInstance;
import cn.ghost.admin.model.bean.AppPlugin;
import cn.ghost.admin.model.bean.Plugin;
import cn.ghost.admin.model.dto.AppPluginDTO;
import cn.ghost.admin.model.dto.ChangeStatusDTO;
import cn.ghost.admin.model.vo.AppVO;
import cn.ghost.admin.service.AppService;
import cn.ghost.admin.transfer.AppInstanceTransfer;
import cn.ghost.admin.transfer.AppVOTransfer;
import cn.ghost.common.constants.EnabledEnum;
import cn.ghost.common.exception.GatewayException;
import cn.ghost.common.model.ServiceInstance;
import cn.ghost.common.model.dto.AppInfoDTO;
import cn.ghost.common.model.dto.RegisterAppDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:28
 */
@Service
@Slf4j
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;
    @Resource
    private AppInstanceMapper appInstanceMapper;
    @Resource
    private PluginMapper pluginMapper;
    @Resource
    private AppPluginMapper appPluginMapper;

    private Gson gson = new GsonBuilder().create();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterAppDTO registerAppDTO) {
        App app = queryByAppName(registerAppDTO.getAppName());
        // first register if not exist app
        Integer appId = app == null ? addApp(registerAppDTO) : app.getId();
        AppInstance instance = query(appId, registerAppDTO.getIp(), registerAppDTO.getPort(), registerAppDTO.getVersion());
        if (instance == null) {
            AppInstance appInstance = new AppInstance();
            appInstance.setAppId(appId);
            appInstance.setIp(registerAppDTO.getIp());
            appInstance.setPort(registerAppDTO.getPort());
            appInstance.setVersion(registerAppDTO.getVersion());
            appInstance.setCreatedTime(LocalDateTime.now());
            appInstanceMapper.insert(appInstance);
        }
        log.info("register app success,dto:[{}]", gson.toJson(registerAppDTO));
    }

    private App queryByAppName(String appName) {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(App::getAppName, appName);
        App app = appMapper.selectOne(wrapper);
        return app;
    }

    private AppInstance query(Integer appId, String ip, Integer port, String version) {
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId, appId)
                .eq(AppInstance::getVersion, version)
                .eq(AppInstance::getIp, ip)
                .eq(AppInstance::getPort, port);
        return appInstanceMapper.selectOne(wrapper);
    }

    private Integer addApp(RegisterAppDTO registerAppDTO) {
        App app = new App();
        BeanUtils.copyProperties(registerAppDTO, app);
        app.setEnabled(EnabledEnum.ENABLE.getCode());
        app.setCreatedTime(LocalDateTime.now());
        appMapper.insert(app);
        bindPlugins(app);
        return app.getId();
    }

    private void bindPlugins(App app) {
        List<Plugin> plugins = pluginMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(plugins)) {
            throw new GatewayException("must init plugins first!");
        }
        plugins.forEach(p -> {
            AppPlugin appPlugin = new AppPlugin();
            appPlugin.setAppId(app.getId());
            appPlugin.setPluginId(p.getId());
            appPlugin.setEnabled(EnabledEnum.ENABLE.getCode());
            appPluginMapper.insert(appPlugin);
        });
    }

    @Override
    public void unregister(RegisterAppDTO registerAppDTO) {
        App app = queryByAppName(registerAppDTO.getAppName());
        if (app == null) {
            return;
        }
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId, app.getId())
                .eq(AppInstance::getVersion, registerAppDTO.getVersion())
                .eq(AppInstance::getIp, registerAppDTO.getIp())
                .eq(AppInstance::getPort, registerAppDTO.getPort());
        appInstanceMapper.delete(wrapper);
        log.info("unregister app instance success,dto:[{}]", gson.toJson(registerAppDTO));
    }

    @Override
    public List<AppInfoDTO> getAppInfos(List<String> appNames) {
        List<App> apps = getAppList(appNames);
        List<Integer> appIds = apps.stream().map(App::getId).collect(Collectors.toList());
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(AppInstance::getAppId, appIds);
        List<AppInstance> appInstances = appInstanceMapper.selectList(wrapper);
        List<AppPluginDTO> appPluginDTOS = appPluginMapper.queryEnabledPlugins(appIds);
        if (CollectionUtils.isEmpty(appInstances) || CollectionUtils.isEmpty(appPluginDTOS)) {
            log.warn("no app infos....");
            return Lists.newArrayList();
        }
        return buildAppInfos(apps, appInstances, appPluginDTOS);
    }

    private List<AppInfoDTO> buildAppInfos(List<App> apps, List<AppInstance> appInstances, List<AppPluginDTO> appPluginDTOS) {
        List<AppInfoDTO> list = Lists.newArrayList();
        apps.forEach(app -> {
            AppInfoDTO appInfoDTO = new AppInfoDTO();
            appInfoDTO.setAppId(app.getId());
            appInfoDTO.setAppName(app.getAppName());
            appInfoDTO.setEnabled(app.getEnabled());
            List<String> enabledPlugins = appPluginDTOS.stream().filter(r -> r.getAppId().equals(app.getId()))
                    .map(AppPluginDTO::getCode)
                    .collect(Collectors.toList());
            appInfoDTO.setEnabledPlugins(enabledPlugins);
            List<AppInstance> instances = appInstances.stream().filter(r -> r.getAppId().equals(app.getId())).collect(Collectors.toList());
            List<ServiceInstance> serviceList = AppInstanceTransfer.INSTANCE.mapToServiceList(instances);
            appInfoDTO.setInstances(serviceList);
            list.add(appInfoDTO);
        });
        return list;
    }

    private List<App> getAppList(List<String> appNames) {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(App::getAppName, appNames);
        return appMapper.selectList(wrapper);
    }

    @Override
    public List<AppVO> getList() {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        List<App> apps = appMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(apps)) {
            log.warn("no apps....");
            return Lists.newArrayList();
        }
        List<AppVO> appVOS = AppVOTransfer.INSTANCE.mapToVOList(apps);
        List<Integer> appIds = appVOS.stream().map(AppVO::getId).collect(Collectors.toList());
        QueryWrapper<AppInstance> query = Wrappers.query();
        query.lambda().in(AppInstance::getAppId, appIds);
        List<AppInstance> instances = appInstanceMapper.selectList(query);
        if (CollectionUtils.isEmpty(instances)) {
            appVOS.forEach(appVO -> appVO.setInstanceNum(0));
        } else {
            Map<Integer, List<AppInstance>> map = instances.stream().collect(Collectors.groupingBy(AppInstance::getAppId));
            appVOS.forEach(appVO -> appVO.setInstanceNum(map.getOrDefault(appVO.getId(), Lists.newArrayList()).size()));
        }
        return appVOS;
    }

    @Override
    public void updateEnabled(ChangeStatusDTO statusDTO) {
        App app = new App();
        app.setId(statusDTO.getId());
        app.setEnabled(statusDTO.getEnabled());
        appMapper.updateById(app);
    }

    @Override
    public void delete(Integer id) {
        QueryWrapper<AppInstance> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AppInstance::getAppId,id);
        List<AppInstance> instances = appInstanceMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(instances)){
            throw new GatewayException("there is an instance of the service that cannot be deleted!");
        }
        appMapper.deleteById(id);
    }
}
