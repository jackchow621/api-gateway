package cn.ghost.controller;

import cn.ghost.model.Result;
import cn.ghost.model.dto.UpdateWeightDTO;
import cn.ghost.model.vo.InstanceVO;
import cn.ghost.service.AppInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/02 14:42
 */
@RestController("/app/instance")
public class AppInstanceController {
    @Autowired
    private AppInstanceService instanceService;

    @GetMapping("/instanceList")
    public String list(@RequestParam("appId") Integer appId, ModelMap map) {
        List<InstanceVO> instanceVOS = instanceService.queryList(appId);
        map.put("instanceVOS", instanceVOS);
        return "instance";
    }

    @ResponseBody
    @PutMapping("/updateWeight")
    public Result updateWeight(@RequestBody @Validated UpdateWeightDTO updateWeightDTO){
        instanceService.updateWeight(updateWeightDTO);
        return Result.success();
    }
}
