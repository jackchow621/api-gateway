package cn.ghost.admin.mapper;

import cn.ghost.admin.model.bean.AppPlugin;
import cn.ghost.admin.model.dto.AppPluginDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 11:45
 */
public interface AppPluginMapper extends BaseMapper<AppPlugin> {

    List<AppPluginDTO> queryEnabledPlugins(@Param("appIds") List<Integer> appIds);
}
