package com.diane.blog.service;

import com.alibaba.fastjson.JSONArray;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleInfo;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @author dianedi
 * @date 2020/12/12 9:11
 * @Destription
 */
@Service
public interface ArticleService {

//    发布博客（update init）
    int submitArticle(TblArticleInfo articleInfo, TblArticleContent articleContent);
//    更新博客
    int updateArticle(String data);
//    删除博客
    int delArticle(String artId);
//    查看博文详情（标题，正文，创建时间）
    JSONArray listArticleDetail(String artId) throws UnsupportedEncodingException;
//    查看所有博文概览(标题，简介)
    String listAllArticle();
//    展示某分类所有文章
    String listAllArticleInSort(int sortid);
//    展示除随笔分类的其他文章(技术博客文章)
    String listAllBlog();
//    模糊全局查询
    String searchArticleByKeyword(String keyword);
//    获取最近时间的五篇文章
    String recentArticle();
//    获取已删除的文章
    String listdeletedArt();
//    恢复已删除文章
    int recoveryArt(String artId);
//    文章是否被加密 (isTop字段已被修改为是否加密)
    boolean isSecret(String artId);
//    接收输入的密码和已加密过的博文数据对文章进行解密
    JSONArray contentDecrypt(String artId,String pwd);
}
