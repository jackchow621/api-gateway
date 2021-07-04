package cn.ghost.server.cache;

import cn.ghost.common.exception.GatewayException;
import cn.ghost.common.model.dto.AppRuleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 11:36
 */
@Slf4j
public class RouteRuleCache {
    private static final Map<String, CopyOnWriteArrayList<AppRuleDTO>> ROUTE_RULE_MAP = new ConcurrentHashMap<>();

    public static void add(Map<String, List<AppRuleDTO>> map) {
        map.forEach((key, value) -> ROUTE_RULE_MAP.put(key, new CopyOnWriteArrayList(value)));
    }

    public static void remove(Map<String, List<AppRuleDTO>> map) {
        for (Map.Entry<String, List<AppRuleDTO>> entry : map.entrySet()) {
            String appName = entry.getKey();
            List<Integer> ruleIds = entry.getValue().stream().map(AppRuleDTO::getId).collect(Collectors.toList());
            CopyOnWriteArrayList<AppRuleDTO> ruleDTOS = ROUTE_RULE_MAP.getOrDefault(appName, new CopyOnWriteArrayList<>());
            ruleDTOS.removeIf(r -> ruleIds.contains(r.getId()));
            if (CollectionUtils.isEmpty(ruleDTOS)) {
                ROUTE_RULE_MAP.remove(appName);
            } else {
                ROUTE_RULE_MAP.put(appName, ruleDTOS);
            }
        }
    }


    public static List<AppRuleDTO> getRules(String appName) {
        return Optional.ofNullable(ROUTE_RULE_MAP.get(appName))
                .orElseThrow(() -> new GatewayException("please config route rule in gateway-admin first!"));
    }

}
