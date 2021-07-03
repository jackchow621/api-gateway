package cn.ghost.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:20
 */
@Data
public class UserDTO {

    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
