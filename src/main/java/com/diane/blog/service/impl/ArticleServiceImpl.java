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

    @Autowired
    TblArticleContentExample contentExample;

    @Autowired
    TblArticleInfoExample infoExample;

    @Autowired
    TblArticleCategoryMapper articleCategoryMapper;

    @Autowired
    TblArticleCategoryExample articleCategoryExample;

    @Override
    public int submitArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {
        articleContent.setId(0L);
//        查询插入的最后一条articleInfo的id
//        Example还是有很大缺陷啊，没封装函数，很多sql就写不出来。
//        在TblArticleInfoMapper中写了查询
        int a = articleInfoMapper.insertSelective(articleInfo);
        articleContent.setArticleId(articleInfoMapper.selectLast());
        int b = articleContentMapper.insertSelective(articleContent);

        return  a+b ;
          }

    @Override
    public int updateArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {
//        根据文章id查询内容表里的文章内容content
//        System.out.println("id="+articleInfo.getId());
        contentExample.createCriteria().andArticleIdEqualTo(articleInfo.getId());
//      example更新：第一个参数是更新后的数据组成的对象，第二个参数是example构造的查询条件即要更新的属性。
        infoExample.createCriteria().andIdEqualTo(articleInfo.getId());

        return articleContentMapper.updateByExampleSelective(articleContent,contentExample) + articleInfoMapper.updateByExampleSelective(articleInfo,infoExample);
    }

    @Override
    public int delArticle(Long artId) {
        contentExample.createCriteria().andArticleIdEqualTo(artId);
//        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
        List<TblArticleContent> content = articleContentMapper.selectByExample(contentExample);
        int a = articleInfoMapper.deleteByPrimaryKey(artId);
        return  a + articleContentMapper.deleteByPrimaryKey(content.get(0).getId());

    }

    @Override
    public JSONArray listArticleDetail(Long artId) {
        TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(artId);
//        根据文章id查询内容表里的文章内容content
        contentExample.createCriteria().andArticleIdEqualTo(artId);
//      读取text类型的要用selectByExampleWithBLOBs()
        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
        System.out.println("content"+content.size());
         if (content != null){
             System.out.println("content"+content.size());
             List<String> list = new ArrayList<>();
             list.add(articleInfo.getTitle());
             list.add(content.get(0).getContent());
             list.add(articleInfo.getModifiedBy().toString());
             return listToJsonArray(list);
         }else {
             return null;
         }
    }

    @Override
    public JSONObject listAllArticle() {
//        查询全部直接拿infoExample
//        infoExample.createCriteria();
//      @Autowired 不会每次拿来用都会new Bean。直接拿infoExample的话修改完刷新查询出来有问题（不全）

        TblArticleInfoExample info = new TblArticleInfoExample();
        List<TblArticleInfo> infoList = articleInfoMapper.selectByExample(info);
//      标题，简介
        if (infoList != null) {
            Map<String, Long> map = new HashMap<>();
            for (int i = 0; i < infoList.size(); i++) {
                map.put(infoList.get(i).getTitle(), infoList.get(i).getId());
            }
            return mapToJSONObject(map);
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
        List<TblArticleCategory> result = articleCategoryMapper.selectByExample(articleCategoryExample);;
        Map<String, Long> sortmap = new HashMap<>();
        for (int i = 0; i < result.size(); i++){
            TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(result.get(i).getArticleId());
            sortmap.put(articleInfo.getTitle(),articleInfo.getId());
        }
        return mapToJSONObject(sortmap);
    }

    @Override
    public JSONObject listAllBlog() {
        articleCategoryExample.createCriteria().andSortIdNotEqualTo((long) 1);//这里写 1L查不出来
        List<TblArticleCategory> result = articleCategoryMapper.selectByExample(articleCategoryExample);
        Map<String, Long> sortmap = new HashMap<>();
        for (int i = 0; i < result.size(); i++){
            TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(result.get(i).getArticleId());
            sortmap.put(articleInfo.getTitle(),articleInfo.getId());
        }
        return mapToJSONObject(sortmap);
    }

}
