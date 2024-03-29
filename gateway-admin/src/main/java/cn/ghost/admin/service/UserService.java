package cn.ghost.admin.service;


import cn.ghost.admin.model.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:21
 */
public interface UserService {
    void add(UserDTO userDTO);

    void login(UserDTO userDTO, HttpServletResponse response);
}
