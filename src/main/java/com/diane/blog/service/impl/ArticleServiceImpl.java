package com.diane.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleCategoryMapper;
import com.diane.blog.dao.TblArticleContentMapper;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.dao.TblCategoryInfoMapper;
import com.diane.blog.model.*;
import com.diane.blog.service.ArticleService;
import com.diane.blog.util.ReturnCode;
import com.diane.blog.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.diane.blog.util.JsonUtils.listToJsonArray;

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
    TblArticleCategoryMapper articleCategoryMapper;

    @Autowired
    TblCategoryInfoMapper categoryInfoMapper;

    /**
     * artinfo表需要插入一条数据，同时artcontent也需要插入一条数据，这两条数据要么都插入进去，要么都没插，不能出现info中插入成功而content插入失败的情况。
     * 用事务
     * 1.怎么配置@Transactional
     * 2.怎么设置回滚
     * 3.除了查询其他地方可能都涉及回滚
     * @param articleInfo
     * @param articleContent
     * @return
     */
    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int submitArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {

        articleContent.setId(0L);
//        查询插入的最后一条articleInfo的id
//        Example还是有很大缺陷啊，没封装函数，很多sql就写不出来。
//        在TblArticleInfoMapper中写了查询
        int a = articleInfoMapper.insertSelective(articleInfo);
//      写conrent写不进去
        articleContent.setArticleId(articleInfo.getId());
        int b = articleContentMapper.insert(articleContent);
        if (a + b == 2){
            return 2;
        }else if (a + b == 1){
//            两个表如果没有都插入就会事务回滚
            throw new ServiceException(ReturnCode.SQL_DATA_CREATE_EXCEPTION);
        }else {
            return 0;
            }
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int updateArticle(TblArticleInfo articleInfo, TblArticleContent articleContent) {
//        根据文章id查询内容表里的文章内容content
        TblArticleContentExample contentExample = new TblArticleContentExample();
        TblArticleInfoExample infoExample = new TblArticleInfoExample();
        contentExample.createCriteria().andArticleIdEqualTo(articleInfo.getId());
//
//        List<TblArticleContent> contents = articleContentMapper.selectByExample(contentExample);
//        articleContent.setId(contents.get(0).getId());

//      example更新：第一个参数是更新后的数据组成的对象，第二个参数是example构造的查询条件即要更新的属性。
        infoExample.createCriteria().andIdEqualTo(articleInfo.getId());
//        return articleContentMapper.updateByExampleSelective(articleContent,contentExample) + articleInfoMapper.updateByExampleSelective(articleInfo,infoExample);
        int a = articleContentMapper.updateByExampleSelective(articleContent,contentExample);
        int b = articleInfoMapper.updateByExample(articleInfo,infoExample);
        if (a + b == 2){
            return 2;
        }else if (a + b == 1){
            throw new ServiceException(ReturnCode.SQL_DATA_CREATE_EXCEPTION);
        }else {
            return 0;
        }
    }
// 这里需要加某分类下文章数减一的逻辑（这里有点耦合了）
//    查询所删除文章所在分类，使分类信息表中该分类的num-1
    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int delArticle(String artId) {
//        content
        TblArticleInfo delArt = articleInfoMapper.selectByPrimaryKey(artId);
        delArt.setTraffic(1);
        int a = articleInfoMapper.updateByPrimaryKey(delArt);
        int b = articleCategoryMapper.reducecateinfonum(artId);
            if (a + b  == 2){
                return 2;
            }else if (a + b == 1){
                throw new ServiceException(ReturnCode.SQL_DATA_CREATE_EXCEPTION);
            }else {
                return 0;
            }
    }

    @Override
    public JSONArray listArticleDetail(String artId) {
        TblArticleContentExample contentExample = new TblArticleContentExample();
        TblArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(artId);
//        根据文章id查询内容表里的文章内容content
            contentExample.createCriteria().andArticleIdEqualTo(artId);
//      读取text类型的要用selectByExampleWithBLOBs()
            List<TblArticleContent> content = articleContentMapper.selectByExampleWithBLOBs(contentExample);
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
            List<TblArticleInfo> infoList = articleInfoMapper.listArticle();
            if (infoList != null) {
            return JSONObject.toJSONString(infoList);
        }else {
            return null;
        }
    }
    @Override
    public String listAllArticleInSort(int sortid) {
        List<TblArticleInfo> result = articleInfoMapper.listArticleInSort(sortid);
        if(result.size() != 0 && result != null){
            return JSONObject.toJSONString(result);
        }else {
            return null;
        }
    }

    @Override
    public String listAllBlog() {
        TblArticleCategoryExample articleCategoryExample = new TblArticleCategoryExample();
        articleCategoryExample.createCriteria()    //不查预设的三个分类
                .andSortIdGreaterThanOrEqualTo(4);
        List<TblArticleCategory> articleCategories = articleCategoryMapper.selectByExample(articleCategoryExample);
        List<TblArticleInfo> result = new ArrayList<>();
        for (int i = 0; i < articleCategories.size(); i++) {
            result.add(articleInfoMapper.selectByPrimaryKey(articleCategories.get(i).getArticleId()));
        }
        //去除traffic =1 的数据（已删除的）
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getTraffic() ==1){
                result.remove(i);
            }
        }
        if (result.size() != 0 && result != null) {
            return JSONObject.toJSONString(result);
        }else {
            return null;
        }
    }

    @Override
    public String searchArticleByKeyword(String keyword) {
//        要查什么，从article_info 查title，summary，   article_content   查  content
//        先实现，再优化
//        先查title，如果title有关键字直接返回文章概况，若没有查找summary，若匹配到继续返回文章概况，若查找content ，若无返回null
// 1.19
//        全文索引也可以关联多个属性，但我觉得我这样效率高些
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
        if (articleInfoMapper.rencentArticle().size() ==0){
            return null;
        }else {
            return JSONObject.toJSONString(articleInfoMapper.rencentArticle());
        }
    }

    @Override
    public String listdeletedArt() {
        return JSONObject.toJSONString(articleInfoMapper.listDeletedArt());
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public int recoveryArt(String artId) {
        TblArticleInfo recoveryArt = articleInfoMapper.selectByPrimaryKey(artId);
        recoveryArt.setTraffic(0);
        int a = articleInfoMapper.updateByPrimaryKey(recoveryArt);
        int b = articleCategoryMapper.pluscateinfonum(artId);
        if (a + b  == 2){
            return 2;
        }else if (a + b == 1){
            throw new ServiceException(ReturnCode.SQL_DATA_CREATE_EXCEPTION);
        }else {
            return 0;
        }
    }


}
