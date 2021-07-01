package cn.ghost.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 17:56
 */
@TableName("t_app")
@Data
public class App {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String appName;

    private String description;

    private String contextPath;

    private Byte enabled;

    private LocalDateTime createdTime;
}
