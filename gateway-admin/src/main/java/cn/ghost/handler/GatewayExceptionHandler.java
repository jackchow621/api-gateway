package cn.ghost.handler;

import cn.ghost.exception.GatewayException;
import cn.ghost.model.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 17:10
 */
@RestControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Void> handlerBusinessException(Exception exception) {
        return Result.error(transferToShipException(exception));
    }

    private GatewayException transferToShipException(Exception exception) {
        GatewayException shipException;
        if (exception instanceof GatewayException) {
            shipException = (GatewayException) exception;

        } else if (exception instanceof BindException) {
            BindException bindException = (BindException) exception;
            BindingResult bindingResult = bindException.getBindingResult();
            shipException = new GatewayException(getErrorMessage(bindingResult));

        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = validException.getBindingResult();
            shipException = new GatewayException(getErrorMessage(bindingResult));

        } else {
            shipException = new GatewayException(exception.getMessage());
        }
        return shipException;
    }

    private String getErrorMessage(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fieldError -> {
            sb.append(fieldError.getDefaultMessage());
            sb.append("-");
        });
        return sb.toString();
    }
}
