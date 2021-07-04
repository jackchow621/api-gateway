package cn.ghost.admin.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 18:02
 */
@TableName("t_route_rule")
@Data
public class RouteRule {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer appId;

    private String version;

    private String matchObject;

    private String matchKey;

    private Byte matchMethod;

    private String matchRule;

    private Byte enabled;

    private LocalDateTime createdTime;

    private Integer priority;
}
