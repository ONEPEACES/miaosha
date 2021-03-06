package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *@author Xue
 *@date 2018/7/24 16:02
 *@description 全局异常处理
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof BindException) {
            e.printStackTrace();
            BindException bindException = (BindException) e;
            List<ObjectError> allErrors = bindException.getAllErrors();
            ObjectError objectError = allErrors.get(0);
            String defaultMessage = objectError.getDefaultMessage();
            //返回绑定异常的msg
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(defaultMessage));
        }else if(e instanceof GlobalException){
            e.printStackTrace();
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        }else {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
