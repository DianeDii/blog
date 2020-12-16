package com.diane.blog.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.model.TblCategoryInfo;
import org.springframework.stereotype.Service;

/**
 * @author dianedi
 * @date 2020/12/14 10:16
 * @Destription 分类管理
 */
@Service
public interface SortService {
    //crud
    int createSort(TblCategoryInfo categoryInfo);

    int delSort(Long id);
    int delSort(String categoryName);

    JSONArray listSort();
}
