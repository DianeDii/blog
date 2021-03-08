package com.diane.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleCategoryMapper;
import com.diane.blog.dao.TblArticleContentMapper;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.dao.TblCategoryInfoMapper;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleContentExample;
import com.diane.blog.model.TblArticleInfo;
import com.diane.blog.model.TblArticleInfoExample;
import com.diane.blog.service.ArticleService;
import com.diane.blog.util.AESUtils;
import com.diane.blog.util.ReturnCode;
import com.diane.blog.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
        int a = articleInfoMapper.insertSelective(articleInfo);
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
    public int updateArticle(String data) {
        JSONObject updateData = JSONObject.parseObject(data);
        String title = updateData.get("title").toString();
        String summary = updateData.get("summary").toString();
        String content = updateData.get("content").toString();
        String artId = updateData.get("artid").toString();
        Date today = new Date();

        int a = articleInfoMapper.updateInfoData(artId,title,summary,today);
        int b = articleContentMapper.updateContentData(artId,content,today);
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
//      d7f8fce260264d8bafc7e0855cd9822e
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
//                加密content

                if (articleInfo.getIsTop() == true){
                    String tmp = AESUtils.encrypt(content.get(0).getContent());
                    list.add(tmp);
                }else {
                    list.add(content.get(0).getContent());
                }
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
        List<TblArticleInfo> result = articleInfoMapper.listBlog();
        if (result.size() != 0 && result != null) {
            return JSONObject.toJSONString(result);
        }else {
            return null;
        }
    }

    @Override
    public String searchArticleByKeyword(String keyword) {
//        先查title，如果title有关键字直接返回文章概况，若没有查找summary，若匹配到继续返回文章概况，若查找content ，若无返回null
        TblArticleInfoExample infoExample = new TblArticleInfoExample();
        infoExample.createCriteria().andTitleLike("%"+keyword+"%");
        if (articleInfoMapper.selectByExample(infoExample).size() == 0){
            infoExample.clear();
            infoExample.createCriteria().andSummaryLike("%"+keyword+"%");
            if (articleInfoMapper.selectByExample(infoExample).size() ==0){
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

    @Override
    public boolean isSecret(String artId) {
//        先判断artId是否存在
        if (articleInfoMapper.selectByPrimaryKey(artId) != null){
            return articleInfoMapper.artIsSecret(artId);
        }else {
            throw new ServiceException(ReturnCode.NOT_FOUND);
        }
    }

    @Override
    public JSONArray contentDecrypt(String artId, String pwd) {
        if (pwd.equals(AESUtils.PASSWORD_KEY)){
            List<String> list = new ArrayList<>();
            for (int i = 0; i < listArticleDetail(artId).size(); i++) {
                if (i == 1){
                    list.add(AESUtils.decrypt(listArticleDetail(artId).get(1).toString()));
                }else {
                    list.add(listArticleDetail(artId).get(i).toString());
                }
            }
            return listToJsonArray(list);
        }else {
            return null;
        }

    }
}
