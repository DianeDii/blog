package com.diane.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleCategoryMapper;
import com.diane.blog.dao.TblArticleContentMapper;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.model.*;

import com.diane.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.diane.blog.util.JsonUtils.listToJsonArray;
import static com.diane.blog.util.JsonUtils.mapToJSONObject;

/**
 * @author dianedi
 * @date 2020/12/12 16:41
 * @Destription
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    TblArticleInfoMapper articleInfoMapper;

    @Autowired
    TblArticleContentMapper articleContentMapper;
//
//    @Autowired
//    TblArticleContentExample contentExample;
//
//    @Autowired
//    TblArticleInfoExample infoExample;

    @Autowired
    TblArticleCategoryMapper articleCategoryMapper;
//
//    @Autowired
//    TblArticleCategoryExample articleCategoryExample;

    @Override
    public int submitArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {
        articleContent.setId(0L);
//        查询插入的最后一条articleInfo的id
//        Example还是有很大缺陷啊，没封装函数，很多sql就写不出来。
//        在TblArticleInfoMapper中写了查询
        int a = articleInfoMapper.insertSelective(articleInfo);

//      写conrent写不进去
        articleContent.setArticleId(articleInfoMapper.selectLast());
        int b = articleContentMapper.insert(articleContent);

        return  a+b ;
          }

    @Override
    public int updateArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {
//        根据文章id查询内容表里的文章内容content
//        System.out.println("id="+articleInfo.getId());
        TblArticleContentExample contentExample = new TblArticleContentExample();
        TblArticleInfoExample infoExample = new TblArticleInfoExample();
        contentExample.createCriteria().andArticleIdEqualTo(articleInfo.getId());
//      example更新：第一个参数是更新后的数据组成的对象，第二个参数是example构造的查询条件即要更新的属性。
        infoExample.createCriteria().andIdEqualTo(articleInfo.getId());
//     这里使用Selective数据库会报错：
        /*
org.springframework.dao.DuplicateKeyException:
### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '0' for key 'PRIMARY'
### The error may involve com.diane.blog.dao.TblArticleContentMapper.updateByExampleSelective-Inline
### The error occurred while setting parameters
### SQL: update tbl_article_content      SET id = ?,                       article_id = ?,                       create_by = ?,                       modifield_by = ?,                       content = ?                          WHERE (  article_id = ? )
### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '0' for key 'PRIMARY'

         */
//        return articleContentMapper.updateByExampleSelective(articleContent,contentExample) + articleInfoMapper.updateByExampleSelective(articleInfo,infoExample);
        return articleContentMapper.updateByExample(articleContent,contentExample) + articleInfoMapper.updateByExample(articleInfo,infoExample);
    }

    @Override
    public int delArticle(Long artId) {
        TblArticleContentExample contentExample = new TblArticleContentExample();
        contentExample.createCriteria().andArticleIdEqualTo(artId);
//        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
        List<TblArticleContent> content = articleContentMapper.selectByExample(contentExample);
        if (content.size() == 0){
          return 3;
        }else {
            int b = articleContentMapper.deleteByPrimaryKey(content.get(0).getId());
            int a = articleInfoMapper.deleteByPrimaryKey(artId);
            return a + b;
        }
    }

    @Override
    public JSONArray listArticleDetail(Long artId) {
        TblArticleContentExample contentExample = new TblArticleContentExample();
        TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(artId);
//        根据文章id查询内容表里的文章内容content
        contentExample.createCriteria().andArticleIdEqualTo(artId);
//      读取text类型的要用selectByExampleWithBLOBs()
        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
        System.out.println("content= "+content.size());
         if (content.size()!= 0){
             List<String> list = new ArrayList<>();
             list.add(articleInfo.getTitle());
             list.add(content.get(0).getContent());
             list.add(articleInfo.getModifiedBy().toString());
             list.add(articleInfo.getSummary());
             return listToJsonArray(list);
         }else {
             return null;
         }
    }

    @Override
    public String listAllArticle() {
//        查询全部直接拿infoExample
//        infoExample.createCriteria();
//      @Autowired 不会每次拿来用都会new Bean。直接拿infoExample的话修改完刷新查询出来有问题（不全）

        TblArticleInfoExample info = new TblArticleInfoExample();
        List<TblArticleInfo> infoList = articleInfoMapper.selectByExample(info);
//      标题，id
// TODO: 2021/1/6 扩展接口，使之增加返回 ‘简介&时间’
//        if (infoList != null) {
//            Map<String, Long> map = new HashMap<>();
//            for (int i = 0; i < infoList.size(); i++) {
//                map.put(infoList.get(i).getTitle(), infoList.get(i).getId());
//            }
//            return mapToJSONObject(map);
//        }else {
//            return null;
//        }
//  我直接list转JSONArray
        if (infoList != null) {
            return JSONObject.toJSONString(infoList);
        }else {
            return null;
        }
    }
//BUG：第一次查询时正确的，之后的查询结果就跟第一次一样
//    是因为第一次执行方法ioc new articleCategoryExample(),在第二次调用这个方法时，ioc就用的还是上一次的对象，没有new，
//    就导致第二次参数变了后查到的结果跟上次一样
//    为什么IOC没有在第二次执行方法的时候new对象呢？
//    myBatis缓存的问题 X
    @Override
    public JSONObject listAllArticleInSort(Long sortid) {
        TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();
//        两次查询的sql一样
//        System.out.println("初始的Example"+articleCategoryExample);
        articleCategoryExample.createCriteria().andSortIdEqualTo(sortid);
        List<TblArticleCategory> result = articleCategoryMapper.selectByExample(articleCategoryExample);
        if (result.size() == 0){
            return null;
        }else {
            Map<String, Long> sortmap = new HashMap<>();
            for (int i = 0; i < result.size(); i++) {
                TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(result.get(i).getArticleId());
                sortmap.put(articleInfo.getTitle(), articleInfo.getId());
            }
            return mapToJSONObject(sortmap);
        }
    }

    @Override
    public JSONObject listAllBlog() {
        TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();

        articleCategoryExample.createCriteria()    //不查预设的三个分类
                .andSortIdGreaterThanOrEqualTo(4L);
        List<TblArticleCategory> result = articleCategoryMapper.selectByExample(articleCategoryExample);
        Map<String, Long> sortmap = new HashMap<>();
        for (int i = 0; i < result.size(); i++){
            TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(result.get(i).getArticleId());
//            null处理
            if(articleInfo != null) {
                sortmap.put(articleInfo.getTitle(), articleInfo.getId());
                }
            }
        if (sortmap.size() != 0) {
            return mapToJSONObject(sortmap);
        }else {
            return null;
        }
    }

    @Override
    public String searchArticleByKeyword(String keyword) {
//        要查什么，从article_info 查title，summary，   article_content   查  content
//        先实现，再优化
//        先查title，如果title有关键字直接返回文章概况，若没有查找summary，若匹配到继续返回文章概况，若查找content ，若无返回null
        TblArticleInfoExample infoExample = new TblArticleInfoExample();
        infoExample.createCriteria().andTitleLike("%"+keyword+"%");
        if (articleInfoMapper.selectByExample(infoExample).size() == 0){
            infoExample.clear();
            infoExample.createCriteria().andSummaryLike("%"+keyword+"%");
            if (articleInfoMapper.selectByExample(infoExample).size() ==0){

/**1.7
 *                 没有查超大文本的啊，索引怎么用
 *                 使用全文索引(Fulltext)
 *                 数据库新建索引 ： create fulltext index content_word on tbl_article_content(content);
 *                 ---
 *
 *                 数据接结构（树。B树/B+树）
 *                 想用索引->索引用了查不出数据->是不是我格式写的不对->找到是数据库配置的原因->还是查不出来->看看索引原理->B+树->树->数据结构
 *                 我好难
 * 1.8      已解决
 */

// TODO: 2021/1/8 功能实现，代码待优化
                if (articleContentMapper.selectcontentLikeWord(keyword).size() == 0){
                    return null;
                }else {
                    for (int i = 0; i < articleContentMapper.selectcontentLikeWord(keyword).size(); i++) {
                        infoExample.clear();
                        infoExample.createCriteria().andIdEqualTo(articleContentMapper.selectcontentLikeWord(keyword).get(i));
                        return JSONObject.toJSONString(articleInfoMapper.selectByExample(infoExample));
                    }
                    return JSONObject.toJSONString(articleContentMapper.selectcontentLikeWord(keyword).size());
                }
            }else {
                return JSONObject.toJSONString(articleInfoMapper.selectByExample(infoExample));
            }
        }else {
            return JSONObject.toJSONString(articleInfoMapper.selectByExample(infoExample));
        }
    }

    @Override
    public String recentArticle() {
        TblArticleInfoExample infoExample = new TblArticleInfoExample();
        if (articleInfoMapper.rencentArticle().size() ==0){
            return null;
        }else {
            return JSONObject.toJSONString(articleInfoMapper.rencentArticle());
        }
    }


}
