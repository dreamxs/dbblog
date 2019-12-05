package cn.dblearn.blog.auth;

import cn.dblearn.blog.auth.service.ShiroService;
import cn.dblearn.blog.entity.sys.SysUser;
import cn.dblearn.blog.entity.sys.SysUserToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * OAuth2Realm
 * 将用户与权限关联的配置
 *
 *
 * @author bobbi
 * @date 2018/10/07 16:39
 * @email 571002217@qq.com
 * @description Shiro 认证类
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     * 权限认证，即登录过后，每个身份不一定，对应的所能看的页面也不一样。
     * PrincipalCollection是一个身份集合，因为我们可以在Shiro中同时配置多个Realm，所以呢身份信息可能就有多个；
     * 因此其提供了PrincipalCollection用于聚合这些身份信息
     *因为PrincipalCollection聚合了多个，此处最需要注意的是getPrimaryPrincipal，如果只有一个Principal 那么直接返回即可，
     * 如果有多个Principal，则返回第一个（因为内部使用Map存储，所以可以认为是返回任意一个）；
     *
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //权(验证权限时调用)
        //获取登录用户信息
        SysUser user = (SysUser)principals.getPrimaryPrincipal();
        Integer userId = user.getUserId();

        //当前用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }
    /**
     * 认证(登录时调用)
     * 身份认证。即登录通过账号和密码验证登陆人的身份信息。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        SysUserToken tokenEntity = shiroService.queryByToken(accessToken);
        //token失效
        if(tokenEntity == null){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        SysUser user = shiroService.queryUser(tokenEntity.getUserId());
        //账号锁定
        if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 续期
        shiroService.refreshToken(tokenEntity.getUserId(),accessToken);

        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }
}
