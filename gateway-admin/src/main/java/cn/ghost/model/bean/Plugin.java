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
 * @create: 2021/07/02 11:42
 */
@TableName("t_plugin")
@Data
public class Plugin {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private String description;

    private LocalDateTime createdTime;
}
