package com.yzw.platform.entity.user;

import java.io.Serializable;

public class InfoRole implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.r_id
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    private Long rId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.r_name
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    private String rName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.r_type
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    private String rType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_role.r_code
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    private String rCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.r_id
     *
     * @return the value of t_role.r_id
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public Long getrId() {
        return rId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.r_id
     *
     * @param rId the value for t_role.r_id
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public void setrId(Long rId) {
        this.rId = rId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.r_name
     *
     * @return the value of t_role.r_name
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public String getrName() {
        return rName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.r_name
     *
     * @param rName the value for t_role.r_name
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public void setrName(String rName) {
        this.rName = rName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.r_type
     *
     * @return the value of t_role.r_type
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public String getrType() {
        return rType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.r_type
     *
     * @param rType the value for t_role.r_type
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public void setrType(String rType) {
        this.rType = rType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_role.r_code
     *
     * @return the value of t_role.r_code
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public String getrCode() {
        return rCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_role.r_code
     *
     * @param rCode the value for t_role.r_code
     *
     * @mbg.generated Fri Jul 12 15:55:26 CST 2019
     */
    public void setrCode(String rCode) {
        this.rCode = rCode;
    }
}