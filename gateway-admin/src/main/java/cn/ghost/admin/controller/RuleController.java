package cn.ghost.admin.controller;

import cn.ghost.admin.model.dto.RuleDTO;
import cn.ghost.admin.service.RuleService;
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
 * @create: 2021/07/01 17:47
 */
@RestController("/rule")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @ResponseBody
    @PostMapping("/addRule")
    public Result add(@Validated RuleDTO ruleDTO){
        ruleService.add(ruleDTO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/deleteRule/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        ruleService.delete(id);
        return Result.success();
    }

    @GetMapping("/rule")
    public String list(ModelMap map, @RequestParam(name = "appName", required = false) String appName) {
        List<cn.ghost.admin.model.vo.RuleVO> ruleVOS = ruleService.queryList(appName);
        map.put("ruleVOS", ruleVOS);
        map.put("appName", appName);
        return "rule";
    }

    @ResponseBody
    @PutMapping("/changeStatus")
    public Result changeStatus(@RequestBody cn.ghost.admin.model.dto.ChangeStatusDTO statusDTO) {
        ruleService.changeStatus(statusDTO);
        return Result.success();
    }
}
