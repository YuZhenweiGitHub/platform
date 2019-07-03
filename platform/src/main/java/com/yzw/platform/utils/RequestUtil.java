package com.yzw.platform.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

@Slf4j
public class RequestUtil {
	/**
	 * 经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，<br>
	 * 服务器端应用也无法直接通过转发请求的地址返回给客户端。 但是在转发请求的HTTP头信息中，<br>
	 * 增加了X－FORWARDED－FOR信息用以跟踪原有的客户端IP地址和原来客户端请求的服务器地址。 原来如此，我们的项目中正好是有前置apache， 将一些请求转发给后端的weblogic，<br>
	 * 看来就是这样导致的。
	 * 
	 * 
	 * @param request
	 * @return 获取真实的IP
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

	/**
	 * 获取当前网站的网络协议
	 * com.qk365.mobile.utils
	 * 方法名：getProtocol
	 * 创建人：peiguangbin
	 * 时间：2018年8月15日-上午11:50:51
	 * @param request
	 * @return String HTTP or HTTPS
	 * @exception
	 * @since  1.0.0
	 */
	public static String getProtocol(HttpServletRequest request) {
		String scheme = request.getHeader("X-Forwarded-Scheme");
		if (null == scheme) {
			scheme = request.getScheme();
		}
		return scheme;
	}

	/**
	 * 获取前端筛选条件缓存key
	 * @param obj bean对象
	 * @return MD5加密字符串
	 * @throws Exception
	 */
	public static String getConditionKey(Object obj) throws Exception {
		Class stuCla = (Class) obj.getClass();
		Field[] fs = stuCla.getDeclaredFields();
		String condition = "";
		for (Field f : fs) {// 遍历属性
			f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
			String attributeName = f.getName();// 得到属性名
			if (attributeName.equals("token") || ("serialVersionUID").equals(attributeName)) {
				continue;
			}
			Object val = f.get(obj);// 得到属性值
			if (val != null && val != "") {
				if (!condition.equals("")) {
					condition += "," + attributeName + ":" + val;
				} else {
					condition += attributeName + ":" + val;
				}
			}
		}
		return MD5Util.createMD5(condition);
	}

	/**
	 * URL编码（utf-8）
	 *
	 */
	public static String encodeUTF8(String sourceUrl) {
		String result = sourceUrl;
		try {
			result = URLEncoder.encode(sourceUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("URL编码失败, e:{}", e);
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(encodeUTF8("http://qingketest.chinacloudapp.cn/scrm/public/js/pdf/test.pdf"));
	}

}
