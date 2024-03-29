package cn.ghost.admin.controller;

import cn.ghost.admin.model.dto.UserDTO;
import cn.ghost.admin.service.UserService;
import cn.ghost.common.constants.AdminConstants;
import cn.ghost.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:18
 */
@RestController("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/addUser")
    public Result add(@RequestBody @Validated UserDTO userDTO) {
        userService.add(userDTO);
        return Result.success();
    }

    @PostMapping("/login")
    public void login(@Validated UserDTO userDTO, HttpServletResponse response) throws IOException {
        userService.login(userDTO, response);
        response.sendRedirect("/app/list");
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(AdminConstants.TOKEN_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "login";
    }
}
