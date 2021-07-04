package cn.ghost.server.spi.loadbalance;

import cn.ghost.common.exception.GatewayException;
import cn.ghost.server.annotation.LoadBalanceAnn;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 11:26
 */
@NoArgsConstructor
public class LoadBalanceFactory {

    private static final Map<String, LoadBalance> LOAD_BALANCE_MAP = new ConcurrentHashMap<>();

    public static LoadBalance getInstance(final String name, String appName, String version) {
        String key = appName + ":" + version;
        return LOAD_BALANCE_MAP.computeIfAbsent(key, (k) -> getLoadBalance(name));
    }

    private static LoadBalance getLoadBalance(String name) {
        ServiceLoader<LoadBalance> loader = ServiceLoader.load(LoadBalance.class);
        Iterator<LoadBalance> iterator = loader.iterator();

        while (iterator.hasNext()) {
            LoadBalance loadBalance = iterator.next();
            LoadBalanceAnn ano = loadBalance.getClass().getAnnotation(LoadBalanceAnn.class);
            Assert.notNull(ano, "load balance name can not be empty!");
            if (name.equals(ano.value())) {
                return loadBalance;
            }
        }
        throw new GatewayException("invalid load balance config");
    }

}
