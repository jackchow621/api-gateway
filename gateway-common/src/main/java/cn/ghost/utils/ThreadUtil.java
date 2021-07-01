package cn.ghost.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 19:05
 */
@AllArgsConstructor
public class ThreadUtil {
    private String name;

    private Boolean daemon;

    public ThreadFactory create() {
        return new ThreadFactoryBuilder().setNameFormat(this.name + "-%d").setDaemon(this.daemon).build();
    }
}
