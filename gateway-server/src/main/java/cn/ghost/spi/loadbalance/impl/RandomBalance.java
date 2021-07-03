package cn.ghost.spi.loadbalance.impl;

import cn.ghost.annotation.LoadBalanceAnn;
import cn.ghost.constants.LoadBalanceConstants;
import cn.ghost.model.ServiceInstance;
import cn.ghost.spi.loadbalance.LoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 11:25
 */
@LoadBalanceAnn(LoadBalanceConstants.RANDOM)
public class RandomBalance implements LoadBalance {

    private static Random random = new Random();

    @Override
    public ServiceInstance chooseOne(List<ServiceInstance> instances) {
        return instances.get(random.nextInt(instances.size()));
    }
}
