package com.yzw.platform.config;

import com.yzw.platform.bo.user.UserInfoBo;
import com.yzw.platform.dao.user.UserInfoMapper;
import com.yzw.platform.entity.user.InfoPermission;
import com.yzw.platform.entity.user.InfoRole;
import com.yzw.platform.entity.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义shiro域
 */
@Slf4j
public class ShiroCustomRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoBo userInfoBo;

    /**
     * 权限相关：身份认证成功后，用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object userName = principalCollection.getPrimaryPrincipal();
        UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        if (null == userName || null == userInfo) {
           return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户角色集合
        List<InfoRole> infoRoles = userInfoBo.selectRoleByUserId(userInfo.getId());
        Set<String> roleSet = infoRoles.stream().map(InfoRole::getrCode).collect(Collectors.toSet());
        info.setRoles(roleSet);
        // 用户权限集合
        List<InfoPermission> permissionList = null;
        if (roleSet.contains("admin")) {
            // 超级管理员,查询所有权限
            permissionList = userInfoBo.selectAllPermission();
        } else {
            // 查询拥有的权限
            permissionList = userInfoBo.selectPermissionByRoleIds(new ArrayList<>(roleSet));
        }
        Set<String> permissions = permissionList.stream().map(InfoPermission::getpCode).collect(Collectors.toSet());
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
        UserInfo userInfo = userInfoBo.selectUserByName(userName);
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