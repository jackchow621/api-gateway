package cn.ghost.interceptor
        ;

import cn.ghost.constants.AdminConstants;
import cn.ghost.constants.GatewayExceptionEnum;
import cn.ghost.exception.GatewayException;
import cn.ghost.model.LocalUser;
import cn.ghost.utils.JwtUtils;
import com.alibaba.druid.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 16:46
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/localUser/login/page");
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AdminConstants.TOKEN_NAME)) {
                token = cookie.getValue();
            }
        }
        if (StringUtils.isEmpty(token)) {
            response.sendRedirect("/localUser/login/page");
            return false;
        }
        boolean result = JwtUtils.checkSignature(token);
        if (!result) {
            throw new GatewayException(GatewayExceptionEnum.TOKEN_ERROR);
        }
        LocalUser localUser = JwtUtils.getPayLoad(token);
        request.setAttribute("currUser", localUser.getName());
        return true;
    }
}
