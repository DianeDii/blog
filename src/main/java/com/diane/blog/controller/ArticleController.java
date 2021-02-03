package com.diane.blog.controller;


import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleInfo;
import com.diane.blog.service.ArticleService;
import com.diane.blog.util.Apiresponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.diane.blog.util.ReturnCode.*;

/**
 * @author dianedi
 * @date 2020/12/12 17:17
 * @Destription
 */
@Api(value = "article_controller",tags = "文章管理接口")
@RequestMapping("/blog")
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    TblArticleInfo articleInfo;

    @Autowired
    TblArticleContent articleContent;

    @Autowired
    TblArticleInfoMapper articleInfoMapper;

    /**
     *创建博客
         * @param data {"title":1,"summary":1,"content":1）
     * @return
     */
    @ApiOperation("文章初始化")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "文章创建成功"),
            @ApiResponse(code = 500,message = "文章信息未创建完全")
    })
    @PostMapping("/init")
    public @ResponseBody
    Apiresponse createArticle(@ApiParam("{'title':1,'summary':1,'content':1}") @RequestBody String data){

//        TblArticleInfo articleInfo = new TblArticleInfo();
//        TblArticleContent articleContent = new TblArticleContent();

//        解析data并插入article，初始化文章
//        json数据传入顺序应与数据库表字段顺序相同
        JSONObject newArticle = JSONObject.parseObject(data);
        articleInfo.setId(0L);
        articleInfo.setTitle(newArticle.get("title").toString());
        articleInfo.setCreateBy(new Date());
        articleInfo.setIsTop(false);
        articleInfo.setModifiedBy(new Date());
        articleInfo.setSummary(newArticle.get("summary").toString());
        articleInfo.setTraffic(0);

//        文章内容应该不能是string格式的

        articleContent.setContent(newArticle.get("content").toString());
        articleContent.setCreateBy(new Date());
        articleContent.setModifieldBy(new Date());

        if (articleService.submitArticle(articleInfo,articleContent) == 2){
//            带回一个新建文章的id
            return  Apiresponse.success(articleInfoMapper.selectLast());
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    /**
     *
     * @param artID
     * @return
     */
    @ApiOperation("文章删除")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "删除成功"),
            @ApiResponse(code = 404,message = "删除文章不存在"),
            @ApiResponse(code = 500,message = "删除文章接口出错"),
            @ApiResponse(code = 501,message = "文章未删除完全"),
    })
    @DeleteMapping("/del")
    public Apiresponse deleteArticle(@ApiParam("文章id") @RequestParam("artID") Long artID){
        int count = articleService.delArticle(artID);
        if (count == 2){
            return Apiresponse.success(SUCCESS);
        }else if(count == -1){
            return Apiresponse.fail(NOT_FOUND);
        }else if (count == 1){
            return Apiresponse.fail(SQL_DATA_CREATE_EXCEPTION);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    /**
     *
     * @param data {"title":1,"summary":1,"content":1 ,"artId":1）
     * @return
     */
    @ApiOperation("更新文章")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "更新成功"),
            @ApiResponse(code = 500,message = "更新失败"),
    })
    @PostMapping("/update")
    public @ResponseBody
    Apiresponse updateArticle(@ApiParam("{'title':1,'summary':1,'content':1 ,'artId':1）") @RequestBody String data){
//        TblArticleInfo articleInfo = new TblArticleInfo();
//        TblArticleContent articleContent = new TblArticleContent();

//        解析data并插入article，初始化文章
//        json数据传入顺序应与数据库表字段顺序相同
        JSONObject updateData = JSONObject.parseObject(data);
        System.out.println(new Date());
        articleInfo.setId(Long.valueOf(updateData.get("artid").toString()));
        articleInfo.setTitle(updateData.get("title").toString());
//        articleInfo.setCreateBy(new Date());
        articleInfo.setIsTop(false);
        articleInfo.setModifiedBy(new Date());
        articleInfo.setSummary(updateData.get("summary").toString());
        articleInfo.setCreateBy(new Date());
        articleInfo.setTraffic(0);

//        文章内容应该不能是string格式的
        articleContent.setContent(updateData.get("content").toString());
        articleContent.setArticleId(Long.valueOf(updateData.get("artid").toString()));
        articleContent.setCreateBy(new Date());
        articleContent.setModifieldBy(new Date());

        if (articleService.updateArticle(articleInfo,articleContent) == 2){
            return  Apiresponse.success(SUCCESS);
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    /**
     * 查看文章详情
     * @param artID
     * @return
     */
    @ApiOperation("查看文章详情")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "查看成功"),
            @ApiResponse(code = 404,message = "文章不存在"),
            @ApiResponse(code = 200,message = "查看成功"),
    })
//    @CrossOrigin(origins = "*",maxAge = 3600)
    @GetMapping("/{artID}")
    public Apiresponse ArticleDetail(@ApiParam("文章id") @PathVariable("artID") Long artID){
        if (articleService == null){
            return Apiresponse.fail(NOT_FOUND);
        }else{
            return Apiresponse.success(articleService.listArticleDetail(artID));
        }
    }
//  解决跨域(在util包种编写了CrosFilter拦截器，全局解决，@CrossOrigin注解只能解决单方法)
//    @CrossOrigin(origins = "*",maxAge = 3600)
    @ApiOperation("展示除.实验室/关于.其他分类的文章")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "展示成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @GetMapping("/list")
    public Apiresponse ListAllArticle(){
//        PageHelper.startPage(pageNo,pageSize);
        if (articleService.listAllArticle() != null){
            return Apiresponse.success(articleService.listAllArticle());
        }else {
            return Apiresponse.fail(NOT_FOUND);
        }
    }
    @ApiOperation("查看某分类下所有文章")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "展示成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @GetMapping("/listbysort/{sortid}")
    public Apiresponse ListbySort(@ApiParam("分类Id")@PathVariable("sortid") Long sortid){
        if (articleService.listAllArticleInSort(sortid) != null){
            return Apiresponse.success(articleService.listAllArticleInSort(sortid));
        }else {
            return Apiresponse.fail(NOT_FOUND);
        }
    }


    @ApiOperation("展示除随笔分类的其他文章(技术博客文章)")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "展示成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @GetMapping("/listBlog")
    public Apiresponse ListBlog(){
        if (articleService.listAllBlog() != null){
            return Apiresponse.success(articleService.listAllBlog());
        }else {
            return Apiresponse.fail(NOT_FOUND);
        }
    }

    @ApiOperation("全局搜索文章内容")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "查询成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @PostMapping("/search")
    public Apiresponse SearchArticleInGlobal(@ApiParam("搜索词") @RequestParam("keyword") String keyword){
        if (articleService.searchArticleByKeyword(keyword) == null){
            return Apiresponse.fail(NOT_FOUND);
        }else {
            return Apiresponse.success(articleService.searchArticleByKeyword(keyword));
        }
    }

    @ApiOperation("展示最近五篇文章")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "查询成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @PostMapping("/recent")
    public Apiresponse recentArticle(){
        if (articleService.recentArticle() ==null){
            return Apiresponse.fail(NOT_FOUND);
        }else {
            return Apiresponse.success(articleService.recentArticle());
        }
    }


}
