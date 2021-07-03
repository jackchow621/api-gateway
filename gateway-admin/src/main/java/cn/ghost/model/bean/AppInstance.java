package cn.ghost.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 18:40
 */
@TableName("t_app_instance")
@Data
public class AppInstance {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer appId;

    private String version;

    private String ip;

    private Integer port;

    private Integer weight;

    private LocalDateTime createdTime;
}
