package cn.yongnian.seckill.exception;

import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TODO
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> ExceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof GlobalException){

            return Result.error(((GlobalException) e).getCodeMessage());
        }
        // BindException 参数绑定错误
        else if(e instanceof BindException){
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String message = error.getDefaultMessage();
            return Result.error(CodeMessage.BIND_ERROR.fillArgs(message));
        }else{
            return Result.error(CodeMessage.SERVER_ERROR);
        }
    }
}
