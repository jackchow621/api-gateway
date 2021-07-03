package cn.ghost.service.impl;

import cn.ghost.constants.AdminConstants;
import cn.ghost.constants.GatewayExceptionEnum;
import cn.ghost.exception.GatewayException;
import cn.ghost.mapper.UserMapper;
import cn.ghost.model.LocalUser;
import cn.ghost.model.bean.User;
import cn.ghost.model.dto.UserDTO;
import cn.ghost.service.UserService;
import cn.ghost.utils.JwtUtils;
import cn.ghost.utils.StringTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:23
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

//    @Value("${gateway.user-password-salt}")
    private String salt;

    @Override
    public void add(UserDTO userDTO) {
        User oldOne = queryByName(userDTO.getUserName());
        if (oldOne != null) {
            throw new GatewayException("the userName already exist");
        }
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(StringTools.md5Digest(userDTO.getPassword(), salt));
        user.setCreatedTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public void login(UserDTO userDTO, HttpServletResponse response) {
        User user = queryByName(userDTO.getUserName());
        if (user == null) {
            throw new GatewayException(GatewayExceptionEnum.LOGIN_ERROR);
        }
        String pwd = StringTools.md5Digest(userDTO.getPassword(), salt);
        if (!pwd.equals(user.getPassword())) {
            throw new GatewayException(GatewayExceptionEnum.LOGIN_ERROR);
        }
        LocalUser localUser = new LocalUser(user.getId(), user.getUserName());
        try {
            String token = JwtUtils.generateToken(localUser);
            Cookie cookie = new Cookie(AdminConstants.TOKEN_NAME, token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            log.error("login error", e);
        }
    }

    private User queryByName(String userName) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.lambda().eq(User::getUserName, userName);
        return userMapper.selectOne(wrapper);
    }
}
