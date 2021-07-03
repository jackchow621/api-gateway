package cn.ghost.cache;

import cn.ghost.model.ServiceInstance;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:44
 */
public class ServiceCache {
    private static final Map<String, List<ServiceInstance>> SERVICE_MAP = new ConcurrentHashMap<>();

    public static List<ServiceInstance> getAllInstances(String serviceName) {
        return SERVICE_MAP.get(serviceName);
    }

    public static void add(String serviceName, List<ServiceInstance> list) {
        SERVICE_MAP.put(serviceName, list);
    }

    public static void removeExpired(List<String> onlineServices) {
        List<String> expiredKeys = Lists.newLinkedList();
        SERVICE_MAP.keySet().forEach(k -> {
            if (!onlineServices.contains(k)) {
                expiredKeys.add(k);
            }
        });
        expiredKeys.forEach(expiredKey -> SERVICE_MAP.remove(expiredKey));
    }
}
