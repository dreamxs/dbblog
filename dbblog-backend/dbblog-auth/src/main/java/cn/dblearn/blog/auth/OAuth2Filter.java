package cn.dblearn.blog.auth;


import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.exception.enums.ErrorEnum;
import cn.dblearn.blog.common.util.HttpContextUtils;
import cn.dblearn.blog.common.util.JsonUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * OAuth2Filter
 * 权限过滤器 AuthenticatingFilter
 * @author bobbi
 * @date 2018/10/07 16:39
 * @email 571002217@qq.com
 * @description
 */
public class OAuth2Filter extends AuthenticatingFilter {


    /**
     * 获得请求中的token
     *
     * */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if(StringUtils.isEmpty(token)){
            return null;
        }
        return new OAuth2Token(token);
    }

    /**
     *
     *该方法是用来判断用户是否已登录，若未登录再判断是否请求的是登录地址，是登录地址则放行，否则返回false终止filter链
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        //同时添加请求方法判断 非法请求过滤掉
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        return false;
    }

    /**
     * isAccessAllowed判断了用户未登录，则会调用onAccessDenied方法做用户登录操作。
     * 若用户请求的不是登录地址，则跳转到登录地址，并且返回false直接终止filter链。若用户请求的是登录地址，
     * 若果是post请求则进行登录操作，由AuthenticatingFilter中提供的executeLogin方法执行。
     * 否则直接通过继续执行filter链，并最终跳转到登录页面（因为用户请求的就是登录地址，
     * 若不是登录地址也会重定向到登录地址）.
     * */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        if(StringUtils.isEmpty(token)){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
            String json = JsonUtils.toJson(Result.error(ErrorEnum.INVALID_TOKEN));
            httpResponse.getWriter().print(json);

            return false;
        }

        return executeLogin(request, response);
    }


    /**
     * 登录失败执行
     * **/
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Result r = Result.error(ErrorEnum.NO_AUTH.getCode(),throwable.getMessage());
            String json = JsonUtils.toJson(r);
            httpResponse.getWriter().print(json);
        } catch (Exception e1) {

        }

        return false;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isEmpty(token)){
            token = httpRequest.getParameter("token");
        }

        return token;
    }


}
