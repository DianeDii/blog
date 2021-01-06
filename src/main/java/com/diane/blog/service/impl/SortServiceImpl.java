package com.diane.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleCategoryMapper;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.dao.TblCategoryInfoMapper;
import com.diane.blog.model.TblArticleCategory;
import com.diane.blog.model.TblArticleCategoryExample;
import com.diane.blog.model.TblCategoryInfo;
import com.diane.blog.model.TblCategoryInfoExample;
import com.diane.blog.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.diane.blog.util.JsonUtils.listToJsonArray;

import java.util.Date;
import java.util.List;

/**
 * @author dianedi
 * @date 2020/12/14 10:25
 * @Destription
 */
@Service
public class SortServiceImpl implements SortService {

    @Autowired
    TblCategoryInfoMapper categoryInfoMapper;

    @Autowired
    TblCategoryInfoExample categoryInfoExample;

    @Autowired
    TblArticleCategoryMapper articleCategoryMapper;


    @Autowired
    TblArticleCategory articleCategory;

    @Autowired
    TblArticleInfoMapper articleInfoMapper;

    @Override
    public int createSort(TblCategoryInfo categoryInfo) {
        if (categoryInfo == null){
            return -1;
        }else {
            return categoryInfoMapper.insert(categoryInfo);
        }
    }

    @Override
    public int delSort(Long id) {
        return categoryInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delSort(String categoryName) {
        categoryInfoExample.createCriteria().andNameEqualTo(categoryName);
        return categoryInfoMapper.deleteByExample(categoryInfoExample);
    }

    @Override
    public JSONArray listSort() {
//        is_effective是否启用字段先不用
        categoryInfoExample.createCriteria();
        List<TblCategoryInfo> allSort= categoryInfoMapper.selectByExample(categoryInfoExample);
        return listToJsonArray(allSort);
    }



    //  文章分类相关
//    增加删除是sortInfo表里也需要同步数量
    @Override
    public int addArticleInSort(Long sortid, Long articleid) {
        articleCategory.setId(0L);
        articleCategory.setSortId(sortid);
        articleCategory.setArticleId(articleid);
        articleCategory.setCreateBy(new Date());
        articleCategory.setModifiedBy(new Date());
        articleCategory.setIsEffective(true);
//      更新信息表
        TblCategoryInfo cateInfo =  categoryInfoMapper.selectByPrimaryKey(sortid);
        cateInfo.setNumber((byte) (cateInfo.getNumber()+1));
        return categoryInfoMapper.updateByPrimaryKey(cateInfo) + articleCategoryMapper.insertSelective(articleCategory);
    }

    @Override
    public int delArticleInSort(Long articleid) {

        TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();

        articleCategoryExample.createCriteria().andArticleIdEqualTo(articleid);
        return articleCategoryMapper.deleteByExample(articleCategoryExample);
    }

    @Override
    public Long getArticleSort(Long artId) {
//        先边界处理，查看有没有这个id
        if (articleInfoMapper.selectByPrimaryKey(artId) == null){
            return -1L;//该文章不存在
        }else {
            TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();
            articleCategoryExample.createCriteria().andArticleIdEqualTo(artId);
                List<TblArticleCategory> articleCategories= articleCategoryMapper.selectByExample(articleCategoryExample);
                if (articleCategories.size() ==1){
                    return articleCategories.get(0).getSortId();
                }else if(articleCategories.size() ==0){
                    return -2L;//该文章无分类信息
                }else {
                    return -3L;//非法！该文章有多条分类信息
                }
//            }
        }
    }


}
