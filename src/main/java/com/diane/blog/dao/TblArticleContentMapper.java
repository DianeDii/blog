package com.diane.blog.dao;

import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblArticleContentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int countByExample(TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int deleteByExample(TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int insert(TblArticleContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int insertSelective(TblArticleContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    List<TblArticleContent> selectByExampleWithBLOBs(TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    List<TblArticleContent> selectByExample(TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    TblArticleContent selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TblArticleContent record, @Param("example") TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") TblArticleContent record, @Param("example") TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TblArticleContent record, @Param("example") TblArticleContentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TblArticleContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(TblArticleContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_article_content
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TblArticleContent record);
}