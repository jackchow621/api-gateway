package cn.ghost.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 11:43
 */
@TableName("t_app_plugin")
@Data
public class AppPlugin {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer appId;

    private Integer pluginId;

    private Byte enabled;
}
