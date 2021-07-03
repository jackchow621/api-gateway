package cn.ghost.exception;

import cn.ghost.constants.GatewayExceptionEnum;
import lombok.Data;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 16:24
 */
@Data
public class GatewayException extends RuntimeException{
    private Integer code;

    private String message;

    public GatewayException(GatewayExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public GatewayException(String message) {
        super(message);
        this.message = message;
        this.code = 5000;
    }

    public GatewayException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public GatewayException(Integer code) {
        this.code = code;
    }

}
