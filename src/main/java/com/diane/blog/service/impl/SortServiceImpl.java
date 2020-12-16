package com.diane.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblCategoryInfoMapper;
import com.diane.blog.model.TblCategoryInfo;
import com.diane.blog.model.TblCategoryInfoExample;
import com.diane.blog.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.diane.blog.util.JsonUtils.listToJsonArray;

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
}
