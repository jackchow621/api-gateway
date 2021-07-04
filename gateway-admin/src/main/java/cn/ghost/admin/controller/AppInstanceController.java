package cn.ghost.admin.controller;

import cn.ghost.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 14:42
 */
@RestController("/app/instance")
public class AppInstanceController {
    @Autowired
    private cn.ghost.admin.service.AppInstanceService instanceService;

    @GetMapping("/instanceList")
    public String list(@RequestParam("appId") Integer appId, ModelMap map) {
        List<cn.ghost.admin.model.vo.InstanceVO> instanceVOS = instanceService.queryList(appId);
        map.put("instanceVOS", instanceVOS);
        return "instance";
    }

    @ResponseBody
    @PutMapping("/updateWeight")
    public Result updateWeight(@RequestBody @Validated cn.ghost.admin.model.dto.UpdateWeightDTO updateWeightDTO){
        instanceService.updateWeight(updateWeightDTO);
        return Result.success();
    }
}
