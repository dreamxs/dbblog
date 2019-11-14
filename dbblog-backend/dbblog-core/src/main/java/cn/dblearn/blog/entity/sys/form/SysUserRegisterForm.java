package cn.dblearn.blog.entity.sys.form;

import lombok.Data;

/**
 * 功能描述: <br>
 * 〈处理注册表单内容信息〉
 *
 * @since: 1.0.0
 * @author:xsping
 * @Date: 2019/11/14 21:25
 */


@Data
public class SysUserRegisterForm {
    private String username;
    private String password;
    private String password2;
    private String captcha;
    private String email;
    private String uuid;
}
