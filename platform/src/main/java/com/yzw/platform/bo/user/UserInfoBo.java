package com.yzw.platform.bo.user;

import com.yzw.platform.dao.user.InfoPermissionMapper;
import com.yzw.platform.dao.user.InfoRoleMapper;
import com.yzw.platform.dao.user.UserInfoMapper;
import com.yzw.platform.entity.user.InfoPermission;
import com.yzw.platform.entity.user.InfoRole;
import com.yzw.platform.entity.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoBo {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private InfoRoleMapper infoRoleMapper;

    @Autowired
    private InfoPermissionMapper infoPermissionMapper;

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return UserInfo
     */
    public UserInfo selectUserByName(String userName) {
        return userInfoMapper.selectUserByName(userName);
    }

    /**
     * 新增用户信息
     * @param userInfo
     * @return
     */
    public int insertUserInfo(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    public int updateUserInfo(UserInfo userInfo) {
        return userInfoMapper.updateByPrimaryKey(userInfo);
    }

    /**
     * 查询用户角色
     * @param uId
     * @return
     */
    public List<InfoRole> selectRoleByUserId(Long uId) {
        return infoRoleMapper.selectRoleByUserId(uId);
    }

    /**
     * 查询所有用户权限
     * @return
     */
    public List<InfoPermission> selectAllPermission() {
        return infoPermissionMapper.selectAll();
    }

    /**
     * 查询用户角色
     * @param roleIds
     * @return
     */
    public List<InfoPermission> selectPermissionByRoleIds(List<String> roleIds) {
        return infoPermissionMapper.selectPermissionByRoleIds(roleIds);
    }
}
