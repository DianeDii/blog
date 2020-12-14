package com.diane.blog.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleContentMapper;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleContentExample;
import com.diane.blog.model.TblArticleInfo;

import com.diane.blog.model.TblArticleInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired(required=false)
    TblArticleContentExample contentExample;

    @Autowired(required=false)
    TblArticleInfoExample infoExample;
    @Override
    public int submitArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {
        int a = articleInfoMapper.insertSelective(articleInfo);
        articleContent.setId(0L);
//        查询插入的最后一条articleInfo的id
//        Example还是有很大缺陷啊，没封装函数，很多sql就写不出来。
//        在TblArticleInfoMapper中写了查询
        articleContent.setArticleId(articleInfoMapper.selectLast());
        int b = articleContentMapper.insertSelective(articleContent);

        if (a == 1 && b ==1){
            return a + b;
        }else {
            return a;
        }    }

    @Override
    public int delArticle(Long artId) {
        int a = articleInfoMapper.deleteByPrimaryKey(artId);
        contentExample.createCriteria().andArticleIdEqualTo(artId);
        List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
        int b = articleContentMapper.deleteByPrimaryKey(content.get(0).getId());
        if (a == 1 && b ==1){
            return a + b;
        }else {
            return a;
        }
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
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < infoList.size(); i++) {
                map.put(infoList.get(i).getTitle(), infoList.get(i).getSummary());
            }

            return mapToJSONObject(map);
        }else {
            return null;
        }
    }
//  分类还没写
    @Override
    public JSONObject listSortAllArticle(Long sortID) {
        return null;
    }
}
