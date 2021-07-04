package cn.ghost.admin.listener;

import cn.ghost.admin.service.AppService;
import cn.ghost.common.constants.EnabledEnum;
import cn.ghost.common.model.NacosMetadata;
import cn.ghost.common.model.ServiceInstance;
import cn.ghost.common.model.dto.AppInfoDTO;
import cn.ghost.common.utils.OkhttpUtils;
import cn.ghost.common.utils.StringTools;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 19:12
 */
@Slf4j
public class NacosSyncTask implements Runnable {
    private NamingService namingService;

    private String url;

    private AppService appService;


    public NacosSyncTask(NamingService namingService, String url, AppService appService) {
        this.namingService = namingService;
        this.url = url;
        this.appService = appService;
    }

    /**
     * Regular update weight,enabled plugins,enabled status to nacos instance
     */
    @Override
    public void run() {
        try {
            // get all app names
            ListView<String> services = namingService.getServicesOfServer(1, Integer.MAX_VALUE, cn.ghost.admin.common.constants.NacosConstants.APP_GROUP_NAME);
            if (CollectionUtils.isEmpty(services.getData())) {
                log.warn("nacos server has no services");
                return;
            }
            List<String> appNames = services.getData();
            List<AppInfoDTO> appInfos = appService.getAppInfos(appNames);
            for (AppInfoDTO appInfo : appInfos) {
                if (CollectionUtils.isEmpty(appInfo.getInstances())) {
                    continue;
                }
                for (ServiceInstance instance : appInfo.getInstances()) {
                    Map<String, Object> queryMap = buildQueryMap(appInfo, instance);
                    String resp = OkhttpUtils.doPut(url, queryMap, "");
                    log.debug("response :{}", resp);
                }
            }

        } catch (Exception e) {
            log.error("nacos sync task error", e);
        }
    }

    private Map<String, Object> buildQueryMap(AppInfoDTO appInfo, ServiceInstance instance) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceName", appInfo.getAppName());
        map.put("groupName", cn.ghost.admin.common.constants.NacosConstants.APP_GROUP_NAME);
        map.put("ip", instance.getIp());
        map.put("port", instance.getPort());
        map.put("weight", instance.getWeight().doubleValue());
        NacosMetadata metadata = new NacosMetadata();
        Gson gson = new GsonBuilder().create();
        metadata.setAppName(appInfo.getAppName());
        metadata.setVersion(instance.getVersion());
        metadata.setPlugins(String.join(",", appInfo.getEnabledPlugins()));
        map.put("metadata", StringTools.urlEncode(gson.toJson(metadata)));
        map.put("ephemeral", true);
        map.put("enabled", EnabledEnum.ENABLE.getCode().equals(appInfo.getEnabled()));
        return map;
    }
}
