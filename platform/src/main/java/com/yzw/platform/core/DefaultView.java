package com.yzw.platform.core;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName DefaultView
 * com.yzw.platform.core
 * @Description
 * @Author yzw
 * @Date 2019/2/22 17:35
 * @Version 1.0.0
 */
public class DefaultView extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry reg) {
        // 默认访问页面
        reg.addViewController("/").setViewName("/security/login");
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE);//最先执行过滤
        super.addViewControllers(reg);
    }
}
