package cn.dblearn.blog.common.exception;

import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.exception.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * MyExceptionHandler
 *
 * @author bobbi
 * @date 2018/10/07 14:33
 * @email 571002217@qq.com
 * @description 统一异常处理器
 *
 * RestControllerAdvice源码可以知道，它就是@ControllerAdvice和@ResponseBody的合并。此注解通过对异常的拦截实现的统一异常返回处理
 * RestControllerAdvice（如果全部异常处理返回json，那么可以使用 @RestControllerAdvice 代替 @ControllerAdvice ，这样在方法上就可以不需要添加 @ResponseBody。）
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        Result result=new Result();
        result.put("code",e.getCode());
        result.put("msg",e.getMsg());
        return result;
    }

    /**
     * 未捕捉异常
     * */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handlerNoFoundException(Exception e){
        log.error(e.getMessage(),e);
        return Result.exception(ErrorEnum.PATH_NOT_FOUND).put("path",e.toString().replace("org.springframework.web.servlet.NoHandlerFoundException: ",""));
    }

    /**
     * 重复键值对异常
     * */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(),e);
        return Result.exception(ErrorEnum.DUPLICATE_KEY);
    }

    /**
     * 权限异常
     * */
    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorizationException(AuthorizationException e){
        log.error(e.getMessage(),e);
        return Result.exception(ErrorEnum.NO_AUTH);
    }

    /**
     * 所有异常
     * */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error(e.getMessage(),e);
        return Result.exception().put("msg",e.toString());
    }



}
