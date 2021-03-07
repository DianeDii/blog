package com.diane.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.diane.blog.model.TblCategoryInfo;
import com.diane.blog.service.SortService;
import com.diane.blog.util.Apiresponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.diane.blog.util.ReturnCode.*;

/**
 * @author dianedi
 * @date 2020/12/14 13:42
 * @Destription
 */
@Api(value = "sort_controller",tags = "分类管理接口")
@RequestMapping("/sort")
@RestController
public class SortController {

    final Byte initNum = 0;

    @Autowired
    SortService sortService;

    @Autowired
    TblCategoryInfo categoryInfo;

    @ApiOperation("创建分类")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "分类创建成功"),
            @ApiResponse(code = 500,message = "分类信息未创建完全"),
            @ApiResponse(code = 501,message = "分类创建失败"),
    })
    @PostMapping("/create")
    public Apiresponse addSort(@ApiParam("分类名称") @RequestParam("sortName") String sortName) {
        categoryInfo.setName(sortName);
        categoryInfo.setCreateBy(new Date());
        categoryInfo.setIsEffective(true);
        categoryInfo.setModifiedBy(new Date());
        categoryInfo.setNumber(initNum);
        if (sortService.createSort(categoryInfo) == 1){
            return Apiresponse.success(SUCCESS);
        }else if (sortService.createSort(categoryInfo) == -1){
            return Apiresponse.fail(SQL_DATA_CREATE_EXCEPTION);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }


    @ApiOperation("删除分类")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "删除成功"),
            @ApiResponse(code = 404,message = "所删除分类不存在"),
            @ApiResponse(code = 500,message = "删除分类接口出错"),
            @ApiResponse(code = 501,message = "分类未删除完全"),
    })
    @DeleteMapping("/del")
    public Apiresponse delSort(@ApiParam("分类Id") @RequestParam("sortId") String Id) {
        int sortId = Integer.parseInt(Id);
        if (sortService.delSort(sortId) == 1){
            return Apiresponse.success(SUCCESS);
        }else if (sortService.delSort(sortId) == 0){
            return Apiresponse.fail(NOT_FOUND);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    @ApiOperation("展示所有分类(不含预设分类)")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "展示成功"),
            @ApiResponse(code = 404,message = "无该分类"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @GetMapping("list")
    public Apiresponse listSort(){
        if (sortService.listSort() != null){
            return Apiresponse.success(sortService.listSort());
        }else {
           return Apiresponse.fail(NOT_FOUND);
        }
    }

    @ApiOperation("给文章添加分类")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "添加成功"),
            @ApiResponse(code = 500,message = "添加失败"),
            @ApiResponse(code = 501,message = "未添加完全")
    })
    @PostMapping("/add")
    public @ResponseBody
    Apiresponse addArticleInSort(@ApiParam("{'sortId': xx,'articleId': xx}") @RequestBody String data){
        JSONObject addSortInfo = JSONObject.parseObject(data);
        System.out.println(addSortInfo.size());
        if (sortService.addArticleInSort(Integer.valueOf(addSortInfo.get("sortId").toString()),addSortInfo.get("articleId").toString()) == 2){
            return Apiresponse.success(SUCCESS);
        }else if (sortService.addArticleInSort((int) addSortInfo.get("sortId"),addSortInfo.get("articleId").toString()) == 1){
            return Apiresponse.fail(SQL_DATA_CREATE_EXCEPTION);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    @ApiOperation("为文章更新分类信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "更新成功"),
            @ApiResponse(code = 500,message = "更新失败"),
    })
    @PostMapping("/artupdate")
    public @ResponseBody Apiresponse updateArticleSort(@ApiParam("{'sortid': xx,'articleId': xx}") @RequestBody String data){
        JSONObject newArtSortInfo = JSONObject.parseObject(data);
        if (sortService.updateArtSortInfo(Integer.valueOf(newArtSortInfo.get("sortId").toString()),newArtSortInfo.get("articleId").toString()) == 1){
            return Apiresponse.success(SUCCESS);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    @ApiOperation("给文章移除分类")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "移除成功"),
            @ApiResponse(code = 500,message = "移除失败"),
    })
    @DeleteMapping("/remove")
    public Apiresponse removeArticleInSort(@ApiParam("文章Id") @RequestParam("articleId") String articleId){
        if (sortService.delArticleInSort(articleId) == 1){
            return  Apiresponse.success(SUCCESS);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    @ApiOperation("获取文章的分类信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "展示成功"),
            @ApiResponse(code = 404,message = "该文章无该分类/该文章不存在"),
            @ApiResponse(code = 500,message = "该文章有多条分类信息")
    })
    @GetMapping("/{artID}")
    public Apiresponse findArtSort(@PathVariable("artID") String artID){
        if (sortService.getArticleSort(artID)== null){
            return Apiresponse.fail(NOT_FOUND);
        }else {
            return Apiresponse.success(sortService.getArticleSort(artID));
        }
    }

    @ApiOperation("更新分类信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "更新分类成功"),
            @ApiResponse(code = 500,message = "更新失败"),
            @ApiResponse(code = 501,message = "更新出错，多行数据受影响")
    })
    @PostMapping("/update")
    public @ResponseBody
    Apiresponse updateSortName(@RequestBody String data){
        JSONObject updateData = JSONObject.parseObject(data);
        int result = sortService.updateSortName(Integer.valueOf(updateData.get("sortid").toString()),updateData.get("value").toString());
        if (result == 0){
            return Apiresponse.fail(API_EXCEPTION);
        }else if (result == 1){
            return Apiresponse.success(SUCCESS);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

}
