package com.yzw.platform.enums;

/**
 * 自定义返回消息结果枚举
 */
public enum ResultMessageEnum {

    SUCCESS(0,"请求成功"),
    PARAM_ERROR(1001, "参数验证失败-{0}"),
    USER_NO_LOGIN_ERROR(1002, "用户未登录"),
    USER_NAME_ERROR(1003, "用户名格式非法"),
    PASSWORD_ERROR(1004, "密码格式非法"),
    LOGIN_FAIL_NUMBER_TRANSFINITE_ERROR(1005, "用户名或密码错误次数过多"),
    USER_OR_PASSWORD_ERROR(1006, "用户名或密码错误"),
    USER_NO_EXISTS_ERROR(1007, "未知账户"),
    USER_IS_EXIXTS_ERROR(1008, "该帐号已存在"),
    USER_LOCK_ERROR(1009, "账户已锁定"),
    OLD_PASSWORD_ERROR(1010, "原密码错误"),
    NEW_OLD_PWD_ERROR(1011, "新老密码不能相同"),
    USER_IS_CANCELLATION_ERROR(1012, "该帐号已注销"),
    ILLEGAL_REQUEST_ERROR(1013, "非法请求"),
    SESSION_TIME_OUT_ERROR(1014, "当前页面已失效，请重新登录！"),
    LOGIN_FAIL_ERROR(1015, "登录失败，请稍后再试!!!"),
    UNAUTHORIZED_ERROR(1016, "无权限"),
    AUTHORIZATION_CHECK_FAIL_ERROR(1017, "权限验证失败"),
    SYSTEM_ERROR(-1, "系统异常，请稍后再试!!!");

    private Integer code;

    private String message;

    ResultMessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
