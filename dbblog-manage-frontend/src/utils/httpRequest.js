import Vue from 'vue'
import axios from 'axios'
import router from '@/router'
import { clearLoginInfo } from '@/utils'
import qs from 'qs' // 字符串处理
import merge from 'lodash/merge' // 合并对象工具

const http = axios.create({
  timeout: 1000 * 30,
  withCredentials: true, // 当前请求为跨域类型时是否在请求中协带cookie
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
  // 处理请求之前的配置
  config.headers['token'] = Vue.cookie.get('token') // // 请求头带上token
  return config
}, error => {
  // 请求失败的处理
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(response => {
  window.console.log(response)
  let data = response.data

  if (data) { // 401 token失效
    let code = response.data.code
    window.console.log(code)
    if (code === 404) {
      router.push({ name: '404',
        params: {
          msg: data.msg,
          code: code,
          path: data.path
        }})
    } else if (code === 403) {
      clearLoginInfo()
      router.push({ name: 'login' })
    } else if (data.code === 401 || data.code === 500 || data.code === 501) {
      window.console.log('跳转到error')
      router.push({ name: 'error',
        params: {
          msg: data.msg,
          code: code,
          path: data.path
        }})
    }
  } else {
    clearLoginInfo()
    router.push({ name: 'login' })
  }
  return response
}, error => {
  console.log(error)
  return Promise.reject(error)
})

/**
 * 请求地址处理
 * @param {*} actionName action方法名称
 */
http.adornUrl = (actionName) => {
  // 非生产环境 && 开启代理, 接口前缀统一使用[/proxyApi/]前缀做代理拦截!
  return window.SITE_CONFIG.baseUrl + actionName
}

/**
 * get 请求参数处理
 * @param params
 * @param openDefaultParams
 * @returns {*}
 */
http.adornParams = (params = {}, openDefaultParams = true) => {
  var defaluts = {
    't': new Date().getTime()
  }
  return openDefaultParams ? merge(defaluts, params) : params
}
/**
 * post请求参数处理
 * @param data
 * @param openDefaultdata
 * @param contentType
 * @returns {string}
 */
http.adornData = (data = {}, openDefaultdata = true, contentType = 'json') => {
  var defaults = {
    't': new Date().getTime()
  }
  data = openDefaultdata ? merge(defaults, data) : data
  return contentType === 'json' ? JSON.stringify(data) : qs.stringify(data)
}

export default http
