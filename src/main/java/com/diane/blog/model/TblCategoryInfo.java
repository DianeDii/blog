package com.diane.blog.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class TblCategoryInfo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_category_info.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_category_info.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_category_info.number
     *
     * @mbggenerated
     */
    private Byte number;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_category_info.create_by
     *
     * @mbggenerated
     */
    private Date createBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_category_info.modified_by
     *
     * @mbggenerated
     */
    private Date modifiedBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_category_info.is_effective
     *
     * @mbggenerated
     */
    private Boolean isEffective;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_category_info.id
     *
     * @return the value of tbl_category_info.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_category_info.id
     *
     * @param id the value for tbl_category_info.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_category_info.name
     *
     * @return the value of tbl_category_info.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_category_info.name
     *
     * @param name the value for tbl_category_info.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_category_info.number
     *
     * @return the value of tbl_category_info.number
     *
     * @mbggenerated
     */
    public Byte getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_category_info.number
     *
     * @param number the value for tbl_category_info.number
     *
     * @mbggenerated
     */
    public void setNumber(Byte number) {
        this.number = number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_category_info.create_by
     *
     * @return the value of tbl_category_info.create_by
     *
     * @mbggenerated
     */
    public Date getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_category_info.create_by
     *
     * @param createBy the value for tbl_category_info.create_by
     *
     * @mbggenerated
     */
    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_category_info.modified_by
     *
     * @return the value of tbl_category_info.modified_by
     *
     * @mbggenerated
     */
    public Date getModifiedBy() {
        return modifiedBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_category_info.modified_by
     *
     * @param modifiedBy the value for tbl_category_info.modified_by
     *
     * @mbggenerated
     */
    public void setModifiedBy(Date modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_category_info.is_effective
     *
     * @return the value of tbl_category_info.is_effective
     *
     * @mbggenerated
     */
    public Boolean getIsEffective() {
        return isEffective;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_category_info.is_effective
     *
     * @param isEffective the value for tbl_category_info.is_effective
     *
     * @mbggenerated
     */
    public void setIsEffective(Boolean isEffective) {
        this.isEffective = isEffective;
    }
}