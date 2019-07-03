package com.yzw.platform.core;

import com.yzw.platform.dto.ResultDto;
import com.yzw.platform.enums.ResultMessageEnum;
import com.yzw.platform.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 参数验证异常捕获
     * @param methodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ResultDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        log.info(">>>>>>>>>>methodArgumentNotValidExceptionHandler:{}<<<<<<<<<<",methodArgumentNotValidException.getMessage());
        return ResultUtils.buildErrorCustomMessage(ResultMessageEnum.PARAM_ERROR.getCode(),bindingResult.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 全局异常捕获
     * @param exception
     * @return
     */
    @ExceptionHandler
    public @ResponseBody ResultDto handleException(Exception exception) {
        log.error(">>>>>>>>>>handleException<<<<<<<<<<", exception);
        return ResultUtils.buildErrorMessage();
    }

    /**
     * shiro验证无权限异常
     * @param ex
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody ResultDto handleShiroException(Exception ex) {
        log.info(">>>>>>>>>>handleShiroException:{}<<<<<<<<<<", ex.getMessage());
        return ResultUtils.buildMessageByEnum(ResultMessageEnum.UNAUTHORIZED_ERROR);
    }

    /**
     * shiro权限验证失败异常
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public @ResponseBody ResultDto AuthorizationException(Exception ex) {
        log.info(">>>>>>>>>>AuthorizationException:{}<<<<<<<<<<", ex.getMessage());
        return ResultUtils.buildMessageByEnum(ResultMessageEnum.AUTHORIZATION_CHECK_FAIL_ERROR);
    }
}
