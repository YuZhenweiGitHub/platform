package com.yzw.platform.service.user;

import com.yzw.platform.entity.user.InfoStreet;
import com.yzw.platform.entity.user.UserInfo;
import com.yzw.platform.entity.user.VillageQk;

import java.util.List;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return UserInfo
     */
    UserInfo selectUserByName(String userName);

    /**
     * 新增用户信息
     * @param userInfo
     * @return
     */
    int insertUserInfo(UserInfo userInfo);

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    int updateUserInfo(UserInfo userInfo);

    void insertForeachStreet(List<InfoStreet> infoStreet);

    List<InfoStreet> selectInfoStreet(String streetName);

    int updateStreet(InfoStreet infoStreet);

    List<VillageQk> selectByVillageName(String villageName);
}
