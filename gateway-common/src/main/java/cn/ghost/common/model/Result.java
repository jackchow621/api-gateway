package cn.ghost.common.model;

import cn.ghost.common.constants.GatewayExceptionEnum;
import cn.ghost.common.exception.GatewayException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:13
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
    private int code;

    private String message;

    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result();
        result.setCode(500);
        result.setMessage("fail");
        return result;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> fail(GatewayExceptionEnum gatewayExceptionEnum) {
        Result<T> result = new Result();
        result.setCode(gatewayExceptionEnum.getCode());
        result.setMessage(gatewayExceptionEnum.getMessage());
        return result;
    }


    public static <T> Result<T> error(GatewayException gatewayException) {
        Result<T> result = new Result();
        result.setCode(gatewayException.getCode());
        result.setMessage(gatewayException.getMessage());
        return result;
    }
}
