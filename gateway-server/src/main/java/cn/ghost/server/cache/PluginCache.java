package cn.ghost.server.cache;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 16:28
 */
public class PluginCache {

    private static final Map<String, List<String>> PLUGIN_MAP = new ConcurrentHashMap<>();

    public static boolean isEnabled(String appName, String pluginName) {
        return PLUGIN_MAP.containsKey(appName) ? PLUGIN_MAP.get(appName).contains(pluginName) : false;
    }

    public static void add(String appName, List<String> pluginNames) {
        PLUGIN_MAP.put(appName, pluginNames);
    }

    public static void removeExpired(List<String> appNames) {
        List<String> expiredKeys = Lists.newLinkedList();
        PLUGIN_MAP.keySet().forEach(k -> {
            if (!appNames.contains(k)) {
                expiredKeys.add(k);
            }
        });
        expiredKeys.forEach(expiredKey -> PLUGIN_MAP.remove(expiredKey));
    }
}
