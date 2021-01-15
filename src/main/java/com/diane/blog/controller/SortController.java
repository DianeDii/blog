package com.diane.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.diane.blog.model.TblCategoryInfo;
import com.diane.blog.service.SortService;
import com.diane.blog.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author dianedi
 * @date 2020/12/14 13:42
 * @Destription
 */
@Api(tags = "分类管理接口")
@RequestMapping("/sort")
@RestController
public class SortController {

    Byte initNum = 0;
    @Autowired
    SortService sortService;

    @Autowired
    TblCategoryInfo categoryInfo;

    @ApiOperation("创建分类")
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
    @ApiOperation("删除分类")
    @DeleteMapping("/del")
    public  ApiResponse delSort(@RequestParam("sortId") String Id) {
        Long sortId = Long.parseLong(Id);
        if (sortService.delSort(sortId) == 1){
            return ApiResponse.success();
        }else if (sortService.delSort(sortId) == 0){
            return ApiResponse.fail("删除失败");
        }else {
            return ApiResponse.fail("删除多条");
        }
    }
    @ApiOperation("展示所有分类(不含预设分类)")
    @GetMapping("list")
    public ApiResponse listSort(){
        if (sortService.listSort() != null){
            return ApiResponse.success(sortService.listSort());
        }else {
           return ApiResponse.fail("暂无分类/分类获取失败");
        }
    }

    @ApiOperation("给文章添加分类")
    @PostMapping("/add")
    public @ResponseBody ApiResponse addArticleInSort(@RequestBody String data){
        JSONObject newArticle = JSONObject.parseObject(data);
        System.out.println(newArticle.size());
        if (sortService.addArticleInSort(Long.valueOf(newArticle.get("sortId").toString()),Long.valueOf(newArticle.get("articleId").toString())) == 2){
            return ApiResponse.success("添加成功");
        }else if (sortService.addArticleInSort((long)newArticle.get("sortId"),(long)newArticle.get("articleId")) == 1){
            return ApiResponse.fail("部分未添加");
        }else {
            return ApiResponse.fail("添加失败");
        }
    }

    @ApiOperation("给文章移除分类")
    @DeleteMapping("/remove")
    public ApiResponse removeArticleInSort(@RequestParam("articleId") Long articleId){
        if (sortService.delArticleInSort(articleId) == 1){
            return  ApiResponse.success();
        }else {
            return ApiResponse.fail("删除失败！");
        }
    }
    @ApiOperation("获取文章的分类信息")
    @GetMapping("/{artID}")
    public ApiResponse findArtSort(@PathVariable("artID") Long artID){
        if (sortService.getArticleSort(artID)== -1){
            return ApiResponse.fail("该文章不存在");
        }else if (sortService.getArticleSort(artID)== -2){
            return ApiResponse.fail("该文章没有分类信息");
        }else if (sortService.getArticleSort(artID)== -3){
            return ApiResponse.fail("该文章有多条分类信息");
        }else {
            return ApiResponse.success(sortService.getArticleSort(artID));
        }
    }
    @ApiOperation("更新分类信息")
    @PostMapping("/update")
    public @ResponseBody ApiResponse updateSortName(@RequestBody String data){
        JSONObject newArticle = JSONObject.parseObject(data);
        int result = sortService.updateSortName(Long.valueOf(newArticle.get("sortid").toString()),newArticle.get("value").toString());
        if (result == 0){
            return ApiResponse.fail("更新分类失败");
        }else if (result == 1){
            return ApiResponse.success();
        }else {
            return ApiResponse.fail("更新出错，多行数据受影响");
        }
    }

}
