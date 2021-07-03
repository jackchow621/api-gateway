package cn.ghost.spi.loadbalance;

import cn.ghost.model.ServiceInstance;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 11:23
 */
public interface LoadBalance {
    ServiceInstance chooseOne(List<ServiceInstance> instances);
}
