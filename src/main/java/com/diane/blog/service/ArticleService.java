package com.diane.blog.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleInfo;

import org.springframework.stereotype.Service;

/**
 * @author dianedi
 * @date 2020/12/12 9:11
 * @Destription
 */
@Service
public interface ArticleService {

//    暂存
//    void createArticle(TblArticleContent articleContent);

//    发布博客（update init）
    int submitArticle(TblArticleInfo articleInfo, TblArticleContent articleContent);

//    更新博客
    int updateArticle(TblArticleInfo articleInfo,TblArticleContent articleContent);
//    删除博客
    int delArticle(Long artId);
//    详情：标题，正文，创建时间
//    查看博文详情
    JSONArray listArticleDetail(Long artId);
//    概览 标题，简介
//    查看所有博文概览
    String listAllArticle();
    //    展示某分类所有文章
    String listAllArticleInSort(Long sortid);
    //    展示除随笔分类的其他文章(技术博客文章)
    JSONObject listAllBlog();
    //  模糊全局查询
    String searchArticleByKeyword(String keyword);
//    获取最近时间的五篇文章
    String recentArticle();
}
