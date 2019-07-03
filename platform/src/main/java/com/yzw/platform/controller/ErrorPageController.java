package com.yzw.platform.controller;

import com.yzw.platform.utils.ConstantPath;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/")
    public String index (){
        return ConstantPath.REDIRECT_LOGIN_URL;
    }

    @RequestMapping("/index.html")
    public String skipIndex () {
        return ConstantPath.INDEX_PAGE_URL;
    }

    @RequestMapping("/error")
    public String handleError() {
        return ConstantPath.ERROR_PAGE_URL;
    }
}
