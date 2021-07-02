package cn.ghost.listener;

import cn.ghost.constants.NacosConstants;
import cn.ghost.service.AppService;
import cn.ghost.utils.ThreadUtil;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 19:00
 */
@Configuration
@Slf4j
public class NacosSyncListener implements ApplicationListener<ContextRefreshedEvent> {

    @NacosInjected
    private NamingService namingService;

    @Resource
    private AppService appService;

    @Value("${nacos.discovery.server-addr}")
    private String baseUrl;

    private static ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1, new ThreadUtil("NACOS-SYNC", true).create());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        String url = "http://" + baseUrl + NacosConstants.INSTANCE_UPDATE_PATH;
        threadPoolExecutor.scheduleWithFixedDelay(new NacosSyncTask(namingService, url, appService), 0, 30L, TimeUnit.SECONDS);
    }
}
