package com.yzw.platform.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

//import org.apache.log4j.Logger;

public class SessionUtils {

	/**
	 * 从session中获取信息
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static Object getAttribute(HttpServletRequest request, Serializable key) {
		if (key == null) {
			return null;
		}
		HttpSession session = request.getSession(false);
		if (session != null) {
			return session.getAttribute(key.toString());
		} else {
			return null;
		}

	}

	/**
	 * 往session中存放信息
	 * 
	 * @param request
	 * @param key
	 * @param value
	 */
	public static void setAttribute(HttpServletRequest request, Serializable key, Object value) {
		HttpSession session = request.getSession(true);
		session.setAttribute(key.toString(), value);
	}

	/**
	 * remove session
	 * 
	 * @param request
	 * @param key
	 */
	public static void removeAttribute(HttpServletRequest request, Serializable key) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(key.toString());
		}

	}

	/**
	 * 获取sessionID
	 * 
	 * @param request
	 * @param key
	 */
	public static String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		return session.getId();
	}
}
