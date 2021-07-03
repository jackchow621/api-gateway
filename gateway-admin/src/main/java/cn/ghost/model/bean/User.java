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
 * @create: 2021/07/01 17:25
 */
@TableName("t_user")
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    private LocalDateTime createdTime;

}
