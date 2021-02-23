package com.diane.blog.service;

import com.alibaba.fastjson.JSONArray;
import com.diane.blog.model.TblCategoryInfo;
import org.springframework.stereotype.Service;

/**
 * @author dianedi
 * @date 2020/12/14 10:16
 * @Destription 分类管理
 */
@Service
public interface SortService {
//    分类的crud
    int createSort(TblCategoryInfo categoryInfo);
    int delSort(int id);
    JSONArray listSort();
    int updateSortName(int sortid,String name);

//   文章分类相关操作(ArticleCategory)

//   将文章加分类
    int addArticleInSort(int sortid, String articleid);
//   将文章移出分类
    int delArticleInSort(String articleid);
//   获取某个文章的分类id
    int getArticleSort(String artId);
//  更新文章的分类信息
    int updateArtSortInfo(int sortid,String articleid);
}
