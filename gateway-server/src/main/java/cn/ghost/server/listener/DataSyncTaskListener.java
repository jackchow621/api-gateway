package cn.ghost.server.listener;

import cn.ghost.server.config.ServerConfig;
import cn.ghost.server.websocket.WebsocketSyncCacheServer;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 16:20
 */
@Configuration
@Slf4j
public class DataSyncTaskListener implements ApplicationListener<ContextRefreshedEvent> {
    private static ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1,
            new cn.ghost.admin.common.utils.ThreadUtil("SERVICE-SYNC", true).create());

    @NacosInjected
    private NamingService namingService;

    @Autowired
    private ServerConfig properties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            log.warn("event.getApplicationContext().getParent() != null");
            return;
        }
        scheduledPool.scheduleWithFixedDelay(new DataSyncTask(namingService)
                , 0L, properties.getCacheRefreshInterval(), TimeUnit.SECONDS);
        WebsocketSyncCacheServer websocketSyncCacheServer = new WebsocketSyncCacheServer(properties.getWebSocketHostname(), properties.getWebSocketPort());
        websocketSyncCacheServer.start();
    }
}
