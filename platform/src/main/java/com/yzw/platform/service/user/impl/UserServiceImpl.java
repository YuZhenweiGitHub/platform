package com.yzw.platform.service.user.impl;

import com.yzw.platform.annotation.DS;
import com.yzw.platform.bo.user.UserInfoBo;
import com.yzw.platform.config.DataSourceConfig;
import com.yzw.platform.dao.user.InfoStreetMapper;
import com.yzw.platform.dao.user.VillageQkMapper;
import com.yzw.platform.entity.user.InfoStreet;
import com.yzw.platform.entity.user.UserInfo;
import com.yzw.platform.entity.user.VillageQk;
import com.yzw.platform.service.user.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoBo userInfoBo;

    @Autowired
    private InfoStreetMapper infoStreetMapper;

    @Autowired
    private VillageQkMapper villageQkMapper;

    @Override
    public UserInfo selectUserByName(String userName) {
        return userInfoBo.selectUserByName(userName);
    }

    @Override
    public int insertUserInfo(UserInfo userInfo) {
        return userInfoBo.insertUserInfo(userInfo);
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) {
        return userInfoBo.updateUserInfo(userInfo);
    }

    @Override
    public void insertForeachStreet(List<InfoStreet> infoStreet) {
        infoStreetMapper.insertForeachStreet(infoStreet);
    }

    @Override
    public List<InfoStreet> selectInfoStreet(String streetName) {
        return infoStreetMapper.selectInfoStreet(streetName);
    }

    @Override
    public int updateStreet(InfoStreet infoStreet) {
        return infoStreetMapper.updateByPrimaryKey(infoStreet);
    }

    @Override
    public List<VillageQk> selectByVillageName(String villageName) {
        return villageQkMapper.selectByVillageName(villageName);
    }
}
