<template>
  <div class="site-wrapper site-page--login">
    <div class="site-content__wrapper">
      <div class="site-content">
        <div class="brand-info">
          <h2 class="brand-info__text">猿启</h2>
          <p class="brand-info__intro">欢迎加入猿启博客,你的每一篇博文见证了你的成长的同时促进了他人的进步,分享使人快乐!</p>
        </div>
        <div class="login-main">
          <h3 class="login-title">管理员登录</h3>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
            <el-form-item prop="userName">
              <el-input v-model="dataForm.userName" placeholder="帐号"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" placeholder="密码" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item prop="password2">
              <el-input v-model="dataForm.password2" type="password" placeholder="确认密码" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="20">
                <el-col :span="14">
                  <el-input v-model="dataForm.captcha" placeholder="验证码">
                  </el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()" alt="">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">注册</el-button>
              <a class="login" @click="login()">已有有账号?点此登录</a>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getUUID } from '@/utils'

export default {
  data () {
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.dataForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      dataForm: {
        userName: '',
        password: '',
        password2: '',
        email: '',
        uuid: '',
        captcha: ''
      },
      dataRule: {
        userName: [
          { required: true, message: '帐号不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ],
        password2: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱格式不对', trigger: 'blur', type: 'email' }
        ],
        captcha: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      },
      captchaPath: ''
    }
  },
  created () {
    this.getCaptcha()
  },
  methods: {
    // 提交表单
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl('/admin/sys/register'),
            method: 'post',
            data: this.$http.adornData({
              'username': this.dataForm.userName,
              'password': this.dataForm.password,
              'password2': this.dataForm.password2,
              'uuid': this.dataForm.uuid,
              'email': this.dataForm.email,
              'captcha': this.dataForm.captcha
            })
          }).then(({data}) => {
            if (data && data.code === 200) {
              this.$alert(data.msg + '即将跳转到登录页面', '注册成功', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.replace({ name: 'login' })
                }
              })
            } else {
              this.getCaptcha()
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    // 获取验证码
    getCaptcha () {
      this.dataForm.uuid = getUUID()
      this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.dataForm.uuid}`)
    },
    // 跳转到注册页面
    login () {
      this.$router.replace({ name: 'login' })
    }
  }
}
</script>

<style lang="scss">
  .site-wrapper.site-page--login {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background-color: rgba(38, 50, 56, .6);
    overflow: hidden;
    &:before {
      position: fixed;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100%;
      height: 100%;
      content: "";
      background-image: url(../../assets/img/login_bg.jpg);
      background-size: cover;
    }
    .site-content__wrapper {
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      padding: 0;
      margin: 0;
      overflow-x: hidden;
      overflow-y: auto;
      background-color: transparent;
    }
    .site-content {
      min-height: 100%;
      padding: 30px 500px 30px 30px;
    }
    .brand-info {
      margin: 220px 100px 0 90px;
      color: #fff;
    }
    .brand-info__text {
      margin:  0 0 22px 0;
      font-size: 48px;
      font-weight: 400;
    }
    .brand-info__intro {
      margin: 10px 0;
      font-size: 18px;
      line-height: 1.58;
      opacity: .7;
    }
    .login-main {
      position: absolute;
      top: 0;
      right: 0;
      padding: 150px 60px 180px;
      width: 470px;
      min-height: 100%;
      background-color: #fff;
    }
    .login-title {
      font-size: 16px;
    }
    .login-captcha {
      overflow: hidden;
      > img {
        width: 100%;
        cursor: pointer;
      }
    }
    .login-btn-submit {
      width: 100%;
      margin-top: 38px;
    }
    .login {
      display: block;
      width: 50%;
      text-align: center;
      margin: 0 auto;
      cursor: pointer;
    }
  }
</style>
