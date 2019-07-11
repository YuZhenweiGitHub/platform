package com.yzw.platform.annotation;

import com.yzw.platform.config.DynamicDataSource;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

    String value() default DynamicDataSource.DEFAULT_DS;

}
