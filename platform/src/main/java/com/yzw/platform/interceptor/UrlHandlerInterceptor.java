package com.yzw.platform.interceptor;

import com.alibaba.fastjson.JSON;
import com.yzw.platform.dto.ResultDto;
import com.yzw.platform.enums.ResultMessageEnum;
import com.yzw.platform.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class UrlHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        List<String> excludeUrl = new ArrayList<>();
        String pureUri = request.getRequestURI();
        // 1、session中包含用户信息，放行
        Object userInfo = SessionUtils.getAttribute(request, Constant.PLATFORM_USER_SESSION_KEY);
        if(null != userInfo || excludeUrl.contains(pureUri)){
            return super.preHandle(request, response, handler);
        }
        // 2、判断是否ajax请求
        if (this.isAjax(request)) {
            // 返回错误消息
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(JSON.toJSONString(ResultUtils.buildMessageByEnum(ResultMessageEnum.SESSION_TIME_OUT_ERROR)));
            pw.close();
            response.flushBuffer();
            return false;
        }
        String queryString = request.getQueryString();
        if(!StringUtils.isEmpty(queryString)){
            pureUri = pureUri + "?" + queryString;
        }
        String url = RequestUtil.getProtocol(request) + "://" + request.getServerName() + request.getContextPath() + pureUri;
        String loginUrl = ConstantPath.LOGIN_URL + "?redirectUrl="+ pureUri;
        response.setContentType("text/html;charset=UTF-8");
        // 跳转登录页
        response.sendRedirect(loginUrl);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean isAjax (HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if(!StringUtils.isEmpty(requestType) && requestType.equals("XMLHttpRequest")){
            return true;
        }
        return false;
    }

}
