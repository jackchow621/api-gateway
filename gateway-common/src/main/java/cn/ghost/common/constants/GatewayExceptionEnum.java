package cn.ghost.common.constants;

import lombok.Getter;

@Getter
public enum GatewayExceptionEnum {
    PARAM_ERROR(1000, "param error"),
    SERVICE_NOT_FIND(1001, "service not find,maybe not register"),
    CONFIG_ERROR(1002, "invalid config"),
    LOGIN_ERROR(1003, "userName or password error"),
    NOT_LOGIN(1004, "not login"),
    TOKEN_ERROR(1005, "token error"),

    SYSTEM_ERROR(5000, "system error"),
    NETWORK_TIMEOUT_ERROR(5001, "network timeout");

    private Integer code;

    private String message;

    GatewayExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
