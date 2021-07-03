package cn.ghost;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 16:43
 */
@MapperScan(basePackages = "cn.ghost.mapper")
@SpringBootApplication
public class GatewayAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayAdminApplication.class,args);
    }
}
