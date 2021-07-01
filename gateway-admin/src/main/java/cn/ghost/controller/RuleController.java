package cn.ghost.controller;

import cn.ghost.model.Result;
import cn.ghost.model.dto.ChangeStatusDTO;
import cn.ghost.model.dto.RuleDTO;
import cn.ghost.model.vo.RuleVO;
import cn.ghost.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 17:47
 */
@RestController("/rule")
public class RuleController {

    @Autowired
    RuleService ruleService;

    @ResponseBody
    @PostMapping("/addRule")
    public Result add(@Validated RuleDTO ruleDTO){
        ruleService.add(ruleDTO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        ruleService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public String list(ModelMap map, @RequestParam(name = "appName", required = false) String appName) {
        List<RuleVO> ruleVOS = ruleService.queryList(appName);
        map.put("ruleVOS", ruleVOS);
        map.put("appName", appName);
        return "rule";
    }

    @ResponseBody
    @PutMapping("/status")
    public Result changeStatus(@RequestBody ChangeStatusDTO statusDTO) {
        ruleService.changeStatus(statusDTO);
        return Result.success();
    }
}
