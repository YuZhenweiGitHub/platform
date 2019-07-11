package com.yzw.platform.config;

import com.yzw.platform.entity.user.UserInfo;
import com.yzw.platform.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义shiro域
 */
@Slf4j
public class ShiroCustomRealm extends AuthorizingRealm {

    @Lazy
    @Autowired
    private UserService userService;

    /**
     * 权限相关：身份认证成功后，用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username1 = (String) principalCollection.getPrimaryPrincipal();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户角色集合
        Set<String> roles = new HashSet<>();
        roles.add("staff");
        roles.add("ordinary_admin");
        roles.add("admin");
        info.setRoles(roles);
        // 用户权限集合
        Set<String> permissions = new HashSet<>();
        permissions.add("user:menu_1");
        permissions.add("user:menu_1");
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 身份认证：登录验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("-------身份认证开始--------");
        String userName = (String) authenticationToken.getPrincipal();
        // 1、查询账号信息
        UserInfo userInfo = userService.selectUserByName(userName);
        if (null == userInfo) {
            // 查无此人
            throw new UnknownAccountException();
        }
        // 用户状态验证
        if (userInfo.getUserStatus().intValue() != 0) {
            throw new LockedAccountException();
        }
        return new SimpleAuthenticationInfo(userInfo, userInfo.getPassWord(),
                ByteSource.Util.bytes(userName + "salt"), getName());
    }
}