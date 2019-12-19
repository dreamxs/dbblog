package cn.dblearn.blog.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * HttpContextUtils
 *
 * @author bobbi
 * @date 2018/10/08 19:13
 * @email 571002217@qq.com
 * @description Http上下文
 */
public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public static String getDomain(){
		HttpServletRequest request = getHttpServletRequest();
		StringBuffer url = request.getRequestURL();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	}

	public static String getOrigin(){
		HttpServletRequest request = getHttpServletRequest();
		return request.getHeader("Origin");
	}

	/**
	 * 功能描述: <br>
	 * 〈请求获取指定参数〉
	 *
	 * @param params
	 * 		  Map<String, Object> params,String keyword, String defaultvalue
	 * @return: String
	 * @since: 1.0.0
	 * @Author:xsping
	 * @Date: 2019/11/18 22:35
	 */
	public static String getStrRequest(Map<String, Object> params,String keyword, String defaultvalue) {
		Object obj = params.getOrDefault(keyword,defaultvalue);
		return String.valueOf(obj);
	}

	public static String getStrRequest(Map<String, Object> params,String keyword ) {
		Object obj = params.getOrDefault(keyword,"");
		return String.valueOf(obj);
	}

	/**
	 * 判断是否是Ajax请求
	 *
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null &&
				"XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
	}

	/**
	 * 获取响应状态码
	 * @param request
	 * @return
	 */
	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
