package cn.ghost.client.config;

import cn.ghost.client.listener.AutoRegisterListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/04 11:35
 */
@Configuration
@Import(AutoRegisterListener.class)
@EnableConfigurationProperties(value = {ClientConfig.class})
public class ClientAutoConfigure {
}
