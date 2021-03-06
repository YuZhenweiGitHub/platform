package com.yzw.platform.controller.user;

import com.yzw.platform.core.TokenManager;
import com.yzw.platform.dto.user.UserQueryDto;
import com.yzw.platform.entity.user.UserInfo;
import com.yzw.platform.enums.ResultMessageEnum;
import com.yzw.platform.service.user.UserService;
import com.yzw.platform.utils.Constant;
import com.yzw.platform.utils.ConstantPath;
import com.yzw.platform.utils.ResultUtils;
import com.yzw.platform.utils.SessionUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * subject.isAuthenticated();            // 是否身份验证授权通过
 * subject.isPermitted(permission);      // 验证权限字符串
 * subject.isPermittedAll(permissions);  // 验证权限字符串全部通过
 *
 * @RequiresPermissions (value = { “ sys : user : view ”, “ sys : user : edit ” }, logical = Logical.OR)：表示当前 Subject 需要权限 user:view 或 user:edit。
 * @RequiresRoles(value={“admin”, “user”}, logical=Logical.AND)：表示当前 Subject 需要角色 admin 和 user
 * @RequiresAuthentication：表示当前Subject已经通过login进行了身份验证；
 * @RequiresUser：表示当前 Subject 已经身份验证或者通过记住我登录的。
 * @RequiresGuest：表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
 * @ClassName UserController
 * com.yzw.platform.user
 * @Description
 * @Author yzw
 * @Date 2019/2/22 17:26
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/security")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.html", method = RequestMethod.GET)
    public String skipLogin (HttpServletRequest request,Model model) {
        model.addAttribute("systemName","YZW");
        return ConstantPath.LOGIN_PAGE_URL;
    }

    @RequestMapping(value = "register.html", method = RequestMethod.GET)
    public String skipRegister () {
        return ConstantPath.REGISTER_PAGE_URL;
    }

    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public @ResponseBody Object login (@Validated @RequestBody UserQueryDto queryDto) {
        UserInfo userInfo = null;
        try {
            // 执行认证登陆
            userInfo = TokenManager.login(queryDto.getUserName(),queryDto.getPassWord(),queryDto.getRememberMe());
        } catch (UnknownAccountException uae) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.USER_NO_EXISTS_ERROR);
        } catch (IncorrectCredentialsException ice) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.USER_OR_PASSWORD_ERROR);
        } catch (LockedAccountException lae) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.USER_LOCK_ERROR);
        } catch (ExcessiveAttemptsException eae) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.LOGIN_FAIL_NUMBER_TRANSFINITE_ERROR);
        } catch (AuthenticationException ae) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.USER_OR_PASSWORD_ERROR);
        }
        if (null == userInfo) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.USER_OR_PASSWORD_ERROR);
        }
        return ResultUtils.buildSuccessMessage();
    }

    @RequestMapping(value = "/register.json", method = RequestMethod.POST)
    public @ResponseBody Object register (HttpServletRequest request,@Validated @RequestBody UserQueryDto queryDto) {
        // 1、查询账号信息
        UserInfo userInfo = userService.selectUserByName(queryDto.getUserName());
        if (null != userInfo) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.USER_IS_EXIXTS_ERROR);
        }
        userInfo = new UserInfo();
        // 新增用户
        userService.insertUserInfo(userInfo);
        // 用户信息放入session
        SessionUtils.setAttribute(request, Constant.PLATFORM_USER_SESSION_KEY, userInfo);
        return ResultUtils.buildSuccessMessage();
    }

}