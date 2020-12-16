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
        articleContent.setArticleId(articleInfoMapper.selectLast());
        return articleInfoMapper.insertSelective(articleInfo) + articleContentMapper.insertSelective(articleContent);
          }

    @Override
    public int delArticle(Long artId) {
        contentExample.createCriteria().andArticleIdEqualTo(artId);
        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
        return articleInfoMapper.deleteByPrimaryKey(artId) + articleContentMapper.deleteByPrimaryKey(content.get(0).getId());

    }

    @Override
    public JSONArray listArticleDetail(Long artId) {
        TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(artId);
//        根据文章id查询内容表里的文章内容content
        contentExample.createCriteria().andArticleIdEqualTo(artId);
//      读取text类型的要用selectByExampleWithBLOBs()
        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);

         if (content != null){
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
        infoExample.createCriteria();
        List<TblArticleInfo> infoList = articleInfoMapper.selectByExample(infoExample);
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

    @Override
    public JSONObject listAllArticleInSort(Long sortID) {
        articleCategoryExample.createCriteria().andSortIdEqualTo(sortID);
        List<TblArticleCategory> result = articleCategoryMapper.selectByExample(articleCategoryExample);
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
