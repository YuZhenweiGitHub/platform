package com.yzw.platform.core;

import com.yzw.platform.entity.user.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author yzw
 * @version 1.0
 * Shiro管理下的Token工具类
 */
public class TokenManager {

    //用户session管理
    //public static final CustomSessionManager customSessionManager = SpringContextUtil.getBean("customSessionManager", CustomSessionManager.class);

    /**
     * 获取当前登录的用户User对象
     * @return
     */
    public static UserInfo getToken() {
        UserInfo token = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        return token;
    }


    /**
     * 获取当前用户的Session
     *
     * @return
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 获取当前用户NAME
     *
     * @return
     */
    public static String getUserName() {
        return getToken().getUserName();
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    public static Long getUserId() {
        return getToken() == null ? null : getToken().getId();
    }

    /**
     * 把值放入到当前登录用户的Session里
     *
     * @param key
     * @param value
     */
    public static void setVal2Session(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 从当前登录用户的Session里取值
     *
     * @param key
     * @return
     */
    public static Object getVal2Session(Object key) {
        return getSession().getAttribute(key);
    }

    /**
     * 获取验证码，获取一次后删除
     *
     * @return
     */
    public static String getYZM() {
        String code = (String) getSession().getAttribute("CODE");
        getSession().removeAttribute("CODE");
        return code;
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @param rememberMe
     * @return
     */
    public static UserInfo login(String userName,String password, Boolean rememberMe) throws ShiroException {
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        token.setRememberMe(rememberMe);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        if (!subject.isAuthenticated()) {
            token.clear();
        }
        return getToken();
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return null != SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 退出登录
     */
    public static void loginOut() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 清空当前用户权限信息。
     * 目的：为了在判断权限的时候，再次会再次 <code>doGetAuthorizationInfo(...)  </code>方法。
     * ps：	当然你可以手动调用  <code> doGetAuthorizationInfo(...)  </code>方法。
     * 这里只是说明下这个逻辑，当你清空了权限，<code> doGetAuthorizationInfo(...)  </code>就会被再次调用。
     */
    public static void clearNowUserAuth() {

    }

}
