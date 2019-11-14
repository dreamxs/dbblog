package cn.dblearn.blog.auth.controller;

import cn.dblearn.blog.auth.service.SysCaptchaService;
import cn.dblearn.blog.auth.service.SysUserTokenService;
import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.base.AbstractController;
import cn.dblearn.blog.common.exception.enums.ErrorEnum;
import cn.dblearn.blog.entity.sys.SysUser;
import cn.dblearn.blog.entity.sys.form.SysLoginForm;
import cn.dblearn.blog.entity.sys.form.SysUserRegisterForm;
import cn.dblearn.blog.mapper.sys.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * SysLoginController
 *
 * @author bobbi
 * @date 2018/10/19 18:47
 * @email 571002217@qq.com
 * @description
 */
@RestController
public class AuthSysUserController extends AbstractController {

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @PostMapping("/admin/sys/login")
    public Result login(@RequestBody SysLoginForm form) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            // 验证码不正确
            return Result.error(ErrorEnum.CAPTCHA_WRONG);
        }

        // 用户信息
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser::getUsername, form.getUsername()));
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            // 用户名或密码错误
            return Result.error(ErrorEnum.USERNAME_OR_PASSWORD_WRONG);
        }
        if (user.getStatus() == 0) {
            return Result.error("账号已被锁定，请联系管理员");
        }

        //生成token，并保存到redis
        return sysUserTokenService.createToken(user.getUserId());
    }

    @PostMapping("/admin/sys/register")
    public Result register(@RequestBody SysUserRegisterForm form) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            // 验证码不正确
            return Result.error(ErrorEnum.CAPTCHA_WRONG);
        }

        if (!form.getPassword().equals(form.getPassword2())) {
            return Result.error(ErrorEnum.REGISTER_PassWord_Error_Not_Same);
        }

        // 用户信息
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser::getUsername, form.getUsername()));
        if (user != null) {
            // 用户名或密码错误
            return Result.error(ErrorEnum.REGISTER_USER_EXIST);
        }

        SysUser usermail = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser::getEmail, form.getEmail()));

        if (null != usermail) {
            return Result.error(ErrorEnum.REGISTER_EMAIL_EXIST);
        }
        SysUser registeruser = new SysUser();
        registeruser.setCreateTime(new Date());
        registeruser.setCreateUserId(2);
        registeruser.setCreateUserId(sysUserMapper.queryMaxUserId());
        registeruser.setEmail(form.getEmail());
        registeruser.setStatus(1);
        registeruser.setSalt(UUID.randomUUID().toString());
        registeruser.setUsername(form.getUsername());
        registeruser.setPassword(new Sha256Hash(form.getPassword(),registeruser.getSalt()).toHex());
        sysUserMapper.insert(registeruser);
        return Result.ok("注册成功(oﾟ▽ﾟ)o♡");

    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }

    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public Result logout() {
        sysUserTokenService.logout(getUserId());
        return Result.ok();
    }
}
