package cn.ghost.model;

import cn.ghost.exception.GatewayException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
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


    public static <T> Result<T> error(GatewayException gatewayException) {
        Result<T> result = new Result();
        result.setCode(gatewayException.getCode());
        result.setMessage(gatewayException.getMessage());
        return result;
    }
}
