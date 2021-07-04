package cn.ghost.client.listener;

import cn.ghost.client.config.ClientConfig;
import cn.ghost.constants.AdminConstants;
import cn.ghost.constants.NacosConstants;
import cn.ghost.exception.GatewayException;
import cn.ghost.model.RegisterAppDTO;
import cn.ghost.utils.IpUtil;
import cn.ghost.utils.OkhttpUtils;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/04 11:25
 */
@Slf4j
public class AutoRegisterListener implements ApplicationListener<ContextRefreshedEvent> {

    private volatile AtomicBoolean registered = new AtomicBoolean(false);

    private ClientConfig properties;

    @NacosInjected
    private NamingService namingService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private static List<String> ignoreUrlList = new LinkedList<>();

    static {
        ignoreUrlList.add("/error");
    }

    public AutoRegisterListener(ClientConfig properties) {
        if (!check(properties)) {
            log.error("client config port,contextPath,appName adminUrl and version can't be empty!");
            throw new GatewayException("client config port,contextPath,appName adminUrl and version can't be empty!");
        }
        this.properties = properties;
    }

    private boolean check(ClientConfig properties) {
        return !(properties.getPort() == null || properties.getContextPath() == null
                || properties.getVersion() == null || properties.getAppName() == null
                || properties.getAdminUrl() == null);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!registered.compareAndSet(false, true)) {
            return;
        }
        doRegister();
        registerShutDownHook();
    }

    private void doRegister() {
        Instance instance = new Instance();
        instance.setIp(IpUtil.getLocalIpAddress());
        instance.setPort(properties.getPort());
        instance.setEphemeral(true);
        Map<String, String> metadataMap = new HashMap<>();
        metadataMap.put("version", properties.getVersion());
        metadataMap.put("appName", properties.getAppName());
        instance.setMetadata(metadataMap);
        try {
            namingService.registerInstance(properties.getAppName(), NacosConstants.APP_GROUP_NAME, instance);
        } catch (NacosException e) {
            log.error("register to nacos fail", e);
            throw new GatewayException(e.getErrCode(), e.getErrMsg());
        }
        log.info("register interface info to nacos success!");
        // send register request to ship-admin
        String url = "http://" + properties.getAdminUrl() + AdminConstants.REGISTER_PATH;
        RegisterAppDTO registerAppDTO = buildRegisterAppDTO(instance);
        OkhttpUtils.doPost(url, registerAppDTO);
        log.info("register to gateway-admin success!");
    }

    private RegisterAppDTO buildRegisterAppDTO(Instance instance) {
        RegisterAppDTO registerAppDTO = new RegisterAppDTO();
        registerAppDTO.setAppName(properties.getAppName());
        registerAppDTO.setContextPath(properties.getContextPath());
        registerAppDTO.setIp(instance.getIp());
        registerAppDTO.setPort(instance.getPort());
        registerAppDTO.setVersion(properties.getVersion());
        return registerAppDTO;
    }

    private void registerShutDownHook() {
        final String url = "http://" + properties.getAdminUrl() + AdminConstants.UNREGISTER_PATH;
        final RegisterAppDTO unregisterAppDTO = new RegisterAppDTO();
        unregisterAppDTO.setAppName(properties.getAppName());
        unregisterAppDTO.setVersion(properties.getVersion());
        unregisterAppDTO.setIp(IpUtil.getLocalIpAddress());
        unregisterAppDTO.setPort(properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            OkhttpUtils.doPost(url, unregisterAppDTO);
            log.info("[{}:{}] unregister from gateway-admin success!", unregisterAppDTO.getAppName(), unregisterAppDTO.getVersion());
        }));
    }
}
