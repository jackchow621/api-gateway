package cn.ghost.server.listener;

import cn.ghost.common.constants.GatewayPluginEnum;
import cn.ghost.common.model.ServiceInstance;
import cn.ghost.server.cache.PluginCache;
import cn.ghost.server.cache.ServiceCache;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 16:22
 */
@AllArgsConstructor
@Slf4j
public class DataSyncTask implements Runnable {
    private NamingService namingService;

    @Override
    public void run() {
        try {
            // get all app names
            ListView<String> services = namingService.getServicesOfServer(1, Integer.MAX_VALUE, cn.ghost.admin.common.constants.NacosConstants.APP_GROUP_NAME);
            if (CollectionUtils.isEmpty(services.getData())) {
                log.warn("services {} cannot get any data", services);
                return;
            }
            List<String> appNames = services.getData();
            List<String> onlineAppNames = Lists.newArrayList();
            // get all instances
            for (String appName : appNames) {
                List<Instance> instanceList = namingService.getAllInstances(appName, cn.ghost.admin.common.constants.NacosConstants.APP_GROUP_NAME);
                if (CollectionUtils.isEmpty(instanceList)) {
                    continue;
                }
                ServiceCache.add(appName, buildServiceInstances(instanceList));
                List<String> pluginNames = getEnabledPlugins(instanceList);
                PluginCache.add(appName, pluginNames);
                onlineAppNames.add(appName);
            }
            ServiceCache.removeExpired(onlineAppNames);
            PluginCache.removeExpired(onlineAppNames);

        } catch (NacosException e) {
            log.error("DataSyncTask error:", e);
        }
    }

    private List<ServiceInstance> buildServiceInstances(List<Instance> instanceList) {
        return instanceList.stream().map(instance -> {
            Map<String, String> metadata = instance.getMetadata();
            ServiceInstance serviceInstance = new ServiceInstance();
            serviceInstance.setAppName(metadata.get("appName"));
            serviceInstance.setIp(instance.getIp());
            serviceInstance.setPort(instance.getPort());
            serviceInstance.setVersion(metadata.get("version"));
            serviceInstance.setWeight((int) instance.getWeight());
            return serviceInstance;
        }).collect(Collectors.toList());
    }

    private List<String> getEnabledPlugins(List<Instance> instanceList) {
        Instance instance = instanceList.get(0);
        Map<String, String> metadata = instance.getMetadata();
        String plugins = metadata.getOrDefault("plugins", GatewayPluginEnum.DYNAMIC_ROUTE.getName());
        return Arrays.stream(plugins.split(",")).collect(Collectors.toList());
    }
}
