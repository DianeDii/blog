package com.diane.blog.controller;

import com.diane.blog.model.TblCategoryInfo;
import com.diane.blog.service.SortService;
import com.diane.blog.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author dianedi
 * @date 2020/12/14 13:42
 * @Destription
 */

@RequestMapping("/sort")
@RestController
public class SortController {

    Byte initNum = 0;
    @Autowired
    SortService sortService;

    @Autowired
    TblCategoryInfo categoryInfo;

    @PostMapping("/create")
    public  ApiResponse addSort(@RequestParam("sortName") String sortName) {
        categoryInfo.setName(sortName);
        categoryInfo.setCreateBy(new Date());
        categoryInfo.setIsEffective(true);
        categoryInfo.setModifiedBy(new Date());
        categoryInfo.setNumber(initNum);
        if (sortService.createSort(categoryInfo) == 1){
            return ApiResponse.success();
        }else if (sortService.createSort(categoryInfo) == -1){
            return ApiResponse.fail("categoryInfo 为空");
        }else {
            return ApiResponse.fail("创建失败");
        }
    }
//还可以根据name删，看情况加
    @GetMapping("/del")
    public ApiResponse delSort(@RequestParam("sortId") Long sortId) {
        if (sortService.delSort(sortId) == 1){
            return ApiResponse.success();
        }else if (sortService.delSort(sortId) == 0){
            return ApiResponse.fail("删除失败");
        }else {
            return ApiResponse.fail("删除多条");
        }
    }
    @GetMapping("list")
    public ApiResponse listSort(){
        if (sortService.listSort() != null){
            return ApiResponse.success(sortService.listSort());
        }else {
           return ApiResponse.fail("暂无分类/分类获取失败");
        }
    }

}
