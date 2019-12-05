package cn.dblearn.blog.common.base;

import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.exception.enums.ErrorEnum;
import cn.dblearn.blog.entity.sys.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractController
 * 基础controller类,初始化登录用户
 * <p>
 * 20191201 添加异常信息捕捉并返回异常信息提示
 *
 * @author bobbi
 * @date 2018/10/22 12:35
 * @email 571002217@qq.com
 * @description
 */
public class AbstractController {

    protected SysUser getUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected Integer getUserId() {
        return getUser().getUserId();
    }

}
