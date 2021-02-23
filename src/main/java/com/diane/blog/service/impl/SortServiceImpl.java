package com.diane.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.diane.blog.dao.TblArticleCategoryMapper;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.dao.TblCategoryInfoMapper;
import com.diane.blog.model.TblArticleCategory;
import com.diane.blog.model.TblArticleCategoryExample;
import com.diane.blog.model.TblCategoryInfo;
import com.diane.blog.model.TblCategoryInfoExample;
import com.diane.blog.service.ArticleService;
import com.diane.blog.service.SortService;
import com.diane.blog.util.ReturnCode;
import com.diane.blog.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.diane.blog.util.JsonUtils.listToJsonArray;

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
    TblArticleCategoryMapper articleCategoryMapper;


    @Autowired
    TblArticleCategory articleCategory;

    @Autowired
    TblArticleInfoMapper articleInfoMapper;

    @Autowired
    ArticleService articleService;

    @Override
    public int createSort(TblCategoryInfo categoryInfo) {
        if (categoryInfo == null){
            return -1;
        }else {
            return categoryInfoMapper.insert(categoryInfo);
        }
    }


    /**
     * 如果分类中有内容，删除分类的话其内容也需要删除
     * 判断sort中是否有文章，若有就删文章，前端需要加提示
     * 失败会回滚
     * @param id
     * @return
     */
    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int delSort(int id) {
        TblCategoryInfo sortinfo = categoryInfoMapper.selectByPrimaryKey(id);
        if (sortinfo == null){
            return 0;
        }
        if (sortinfo.getNumber() == 0){
            return categoryInfoMapper.deleteByPrimaryKey(id);
        }else {
//          删除该分类下所有文章，先获取所有需要删除文章的id，整成list，然后用for调用方法
            TblArticleCategoryExample acExample = new TblArticleCategoryExample();
            acExample.createCriteria().andSortIdEqualTo(id);
            List<TblArticleCategory> artlist = articleCategoryMapper.selectByExample(acExample);
            for (int i = 0; i < artlist.size(); i++) {
                articleService.delArticle(artlist.get(i).getArticleId());
            }
//            删完再判断下
            if (sortinfo.getNumber() == 0){
                return categoryInfoMapper.deleteByPrimaryKey(id);
            }else{
                throw new ServiceException(ReturnCode.API_EXCEPTION);
            }
        }
    }

    @Override
    public JSONArray listSort() {
        TblCategoryInfoExample categoryInfoExample = new TblCategoryInfoExample();
//        is_effective是否启用字段先不用
        categoryInfoExample.createCriteria().andIdGreaterThanOrEqualTo(4);
        List<TblCategoryInfo> allSort= categoryInfoMapper.selectByExample(categoryInfoExample);
        return listToJsonArray(allSort);
    }


//    增加删除是sortInfo表里也需要同步数量
    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int addArticleInSort(int sortid, String articleid) {
        articleCategory.setId(0L);
        articleCategory.setSortId(sortid);
        articleCategory.setArticleId(articleid);
        articleCategory.setCreateBy(new Date());
        articleCategory.setModifiedBy(new Date());
        articleCategory.setIsEffective(true);
//      更新信息表
        TblCategoryInfo cateInfo =  categoryInfoMapper.selectByPrimaryKey(sortid);
        cateInfo.setNumber((byte) (cateInfo.getNumber()+1));
        int a = categoryInfoMapper.updateByPrimaryKey(cateInfo);
        int b = articleCategoryMapper.insertSelective(articleCategory);
        if (a + b == 2){
            return 2;
        }else if (a + b == 1){
            throw new ServiceException(ReturnCode.SQL_DATA_CREATE_EXCEPTION);
        }else {
            return 0;
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int delArticleInSort(String articleid) {

        TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();

        articleCategoryExample.createCriteria().andArticleIdEqualTo(articleid);
        int a = articleCategoryMapper.deleteByExample(articleCategoryExample);
//        分类文章内容减一
        articleCategoryMapper.reducecateinfonum(articleid);
        if (a == 1){
            return 1;
        }else {
            throw new ServiceException(ReturnCode.SQL_DATA_CREATE_EXCEPTION);
        }
    }

    @Override
    public int getArticleSort(String artId) {
//        先边界处理，查看有没有这个id
        if (articleInfoMapper.selectByPrimaryKey(artId) == null){
            return -1;//该文章不存在
        }else {
            TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();
            articleCategoryExample.createCriteria().andArticleIdEqualTo(artId);
                List<TblArticleCategory> articleCategories= articleCategoryMapper.selectByExample(articleCategoryExample);
                if (articleCategories.size() ==1){
                    return articleCategories.get(0).getSortId();
                }else if(articleCategories.size() ==0){
                    return -2;//该文章无分类信息
                }else {
                    return -3;//非法！该文章有多条分类信息
                }
        }
    }

    @Override
    public int updateArtSortInfo(int sortid, String articleid) {
        articleCategory.setId(null);
        articleCategory.setSortId(sortid);
        articleCategory.setArticleId(articleid);
        articleCategory.setCreateBy(null);
        articleCategory.setModifiedBy(new Date());
        articleCategory.setIsEffective(null);

        TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();
        articleCategoryExample.createCriteria().andArticleIdEqualTo(articleid);
        int b = articleCategoryMapper.updateByExampleSelective(articleCategory,articleCategoryExample);
        return b;
    }

    @Override
    public int updateSortName(int sortId,String name) {
        TblCategoryInfo info = new TblCategoryInfo();
        info.setNumber(null);
        info.setModifiedBy(null);
        info.setModifiedBy(new Date());
        info.setIsEffective(null);
        info.setId(sortId);
        info.setName(name);
        int a = categoryInfoMapper.updateByPrimaryKeySelective(info);
        return a;
    }

}
