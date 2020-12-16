package com.diane.blog.controller;


import com.alibaba.fastjson.JSONObject;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleInfo;
import com.diane.blog.service.ArticleService;
import com.diane.blog.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author dianedi
 * @date 2020/12/12 17:17
 * @Destription
 */
@RequestMapping("/blog")
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;


    /**
     *创建博客
     * @param data {"title":1,"summary":1,"content":1）
     * @return
     */

    @PostMapping("/init")
    public @ResponseBody ApiResponse createArticle(@RequestBody String data){

        TblArticleInfo articleInfo = new TblArticleInfo();
        TblArticleContent articleContent = new TblArticleContent();

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
            return  ApiResponse.success(null);
        }else {
            return ApiResponse.fail("创建文章异常");
        }
    }

    /**
     *
     * @param artID
     * @return
     */
    @DeleteMapping("/del")
    public ApiResponse deleteArticle(@RequestParam("artID") Long artID){
        int count = articleService.delArticle(artID);
        if (count == 2){
            return ApiResponse.success();
        }else {
            return ApiResponse.fail();
        }
    }

    /**
     * 查看文章详情
     * @param artID
     * @return
     */
    @CrossOrigin(origins = "*",maxAge = 3600)
    @GetMapping("/{artID}")
    public ApiResponse ArticleDetail(@PathVariable("artID") Long artID){
        if (articleService == null){
            return ApiResponse.fail("文章不存在！");
        }else{
            return ApiResponse.success(articleService.listArticleDetail(artID));
        }
    }
//  解决跨域(在util包种编写了CrosFilter拦截器，全局解决，@CrossOrigin注解只能解决单方法)
//    @CrossOrigin(origins = "*",maxAge = 3600)
    @GetMapping("/list")
    public ApiResponse ListAllArticle(){
        if (articleService.listAllArticle() != null){
            return ApiResponse.success(articleService.listAllArticle());
        }else {
            return ApiResponse.fail("无文章/查询失败！");
        }
    }

    @GetMapping("/listbysort")
    public  ApiResponse ListbySort(@RequestParam("sortid") Long sortid){
        if (articleService.listAllArticleInSort(sortid) != null){
            return ApiResponse.success(articleService.listAllArticleInSort(sortid));
        }else {
            return ApiResponse.fail("该分类下无文章/查询失败！");
        }
    }

    @GetMapping("/listBlog")
    public  ApiResponse ListBlog(){
        if (articleService.listAllBlog() != null){
            return ApiResponse.success(articleService.listAllBlog());
        }else {
            return ApiResponse.fail("该分类下无文章/查询失败！");
        }
    }


}