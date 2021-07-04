package cn.ghost.admin.controller;

import cn.ghost.admin.service.AppService;
import cn.ghost.common.model.Result;
import cn.ghost.common.model.dto.RegisterAppDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/02 11:35
 */
@RestController("/app")
public class AppController {
    @Autowired
    private AppService appService;

    @ResponseBody
    @PostMapping("/register")
    public void register(@RequestBody @Validated RegisterAppDTO registerAppDTO) {
        appService.register(registerAppDTO);
    }

    @ResponseBody
    @PostMapping("/unregister")
    public void unregister(@RequestBody RegisterAppDTO unregisterAppDTO) {
        appService.unregister(unregisterAppDTO);
    }

    @GetMapping("/appList")
    public String appList(ModelMap model) {
        List<cn.ghost.admin.model.vo.AppVO> appVOList = appService.getList();
        model.put("appVOList", appVOList);
        return "applist";
    }

    @ResponseBody
    @PutMapping("/updateEnabled")
    public Result updateEnabled(@RequestBody cn.ghost.admin.model.dto.ChangeStatusDTO statusDTO){
        appService.updateEnabled(statusDTO);
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/deleteApp/{id}")
    public Result delete(@PathVariable("id")Integer id){
        appService.delete(id);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/all")
    public Result<List<cn.ghost.admin.model.vo.AppVO>> getAppList(){
        return Result.success(appService.getList());
    }
}
