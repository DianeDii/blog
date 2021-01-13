package com.diane.blog.dao;

import com.diane.blog.model.TblArticleInfo;
import com.diane.blog.model.TblArticleInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface TblArticleInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int countByExample(TblArticleInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int deleteByExample(TblArticleInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int insert(TblArticleInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int insertSelective(TblArticleInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    List<TblArticleInfo> selectByExample(TblArticleInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    TblArticleInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TblArticleInfo record, @Param("example") TblArticleInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TblArticleInfo record, @Param("example") TblArticleInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TblArticleInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TblArticleInfo record);

//    查询最后文章信息表最后一位id
    @Select("SELECT MAX(id) FROM tbl_article_info;")
    Long selectLast();
//    查询最近五篇文章
    @Select("SELECT * FROM tbl_article_info ORDER BY modified_by DESC LIMIT 0,5")
    List<TblArticleInfo> rencentArticle();
}