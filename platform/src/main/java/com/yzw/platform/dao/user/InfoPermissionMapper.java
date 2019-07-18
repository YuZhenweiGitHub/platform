package com.yzw.platform.dao.user;

import com.yzw.platform.entity.user.InfoPermission;
import java.util.List;

public interface InfoPermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_permission
     *
     * @mbg.generated Fri Jul 12 17:25:45 CST 2019
     */
    int deleteByPrimaryKey(Integer pId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_permission
     *
     * @mbg.generated Fri Jul 12 17:25:45 CST 2019
     */
    int insert(InfoPermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_permission
     *
     * @mbg.generated Fri Jul 12 17:25:45 CST 2019
     */
    InfoPermission selectByPrimaryKey(Integer pId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_permission
     *
     * @mbg.generated Fri Jul 12 17:25:45 CST 2019
     */
    List<InfoPermission> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_permission
     *
     * @mbg.generated Fri Jul 12 17:25:45 CST 2019
     */
    int updateByPrimaryKey(InfoPermission record);

    List<InfoPermission> selectPermissionByRoleIds(List<String> roleIds);
}