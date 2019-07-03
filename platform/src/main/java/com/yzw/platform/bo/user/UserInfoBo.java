package com.yzw.platform.bo.user;

import com.yzw.platform.dao.mysql.user.UserInfoMapper;
import com.yzw.platform.entity.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoBo {

    @Autowired
    private UserInfoMapper userInfoMapper;

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
}
