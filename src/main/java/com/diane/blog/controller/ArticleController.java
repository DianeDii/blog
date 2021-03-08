package com.diane.blog.controller;


import com.alibaba.fastjson.JSONObject;
import com.diane.blog.dao.TblArticleInfoMapper;
import com.diane.blog.model.TblArticleContent;
import com.diane.blog.model.TblArticleInfo;
import com.diane.blog.service.ArticleService;
import com.diane.blog.util.Apiresponse;
import com.diane.blog.util.CreateKeyUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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
        JSONObject newArticle = JSONObject.parseObject(data);
        CreateKeyUtil key = new CreateKeyUtil();
        articleInfo.setId(key.getUUIDKey());
        articleInfo.setTitle(newArticle.get("title").toString());
        articleInfo.setCreateBy(new Date());
        articleInfo.setIsTop(false);
        articleInfo.setModifiedBy(new Date());
        articleInfo.setSummary(newArticle.get("summary").toString());
        articleInfo.setTraffic(0);

        articleContent.setContent(newArticle.get("content").toString());
        articleContent.setCreateBy(new Date());
        articleContent.setModifieldBy(new Date());

        if (articleService.submitArticle(articleInfo,articleContent) == 2){
            return  Apiresponse.success(articleInfo.getId());
        }else {
            return Apiresponse.fail(API_EXCEPTION);
        }
    }

    @ApiOperation("文章删除")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "删除成功"),
            @ApiResponse(code = 404,message = "删除文章不存在"),
            @ApiResponse(code = 500,message = "删除文章接口出错"),
            @ApiResponse(code = 501,message = "文章未删除完全"),
    })
    @DeleteMapping("/del")
    public Apiresponse deleteArticle(@ApiParam("文章id") @RequestParam("artID") String artID){
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

        if (articleService.updateArticle(data) == 2){
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
    public Apiresponse ArticleDetail(@ApiParam("文章id") @PathVariable("artID") String artID) throws UnsupportedEncodingException {
        if (articleService == null){
            return Apiresponse.fail(NOT_FOUND);
        }else{
            return Apiresponse.success(articleService.listArticleDetail(artID));
        }
    }

//  解决跨域(在util包种编写了CrosFilter拦截器，全局解决，@CrossOrigin注解只能解决单方法)
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
    public Apiresponse ListbySort(@ApiParam("分类Id")@PathVariable("sortid") int sortid){
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

    @ApiOperation("展示已删除的文章（非实验室/关于））")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "查询成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @PostMapping("/listdeleted")
    public Apiresponse deletedArticle(){
        if (articleService.listdeletedArt() ==null){
            return Apiresponse.fail(NOT_FOUND);
        }else {
            return Apiresponse.success(articleService.listdeletedArt());
        }
    }


    @ApiOperation("恢复已删除文章")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "恢复成功"),
            @ApiResponse(code = 500,message = "恢复失败")
    })
    @PostMapping("/recoveryArt")
    public Apiresponse recoveryArticle(@ApiParam("文章id") @RequestParam("artID") String artID){
        if (articleService.recoveryArt(artID) == 2){
            return Apiresponse.success(SUCCESS);
        }else {
            return Apiresponse.success("无文章");
        }
    }

    @ApiOperation("获取文章加密状态")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "展示成功"),
            @ApiResponse(code = 404,message = "无文章"),
            @ApiResponse(code = 500,message = "查询失败")
    })
    @GetMapping("/isSecret")
    public Apiresponse getBlogSecret(@ApiParam("文章id") @RequestParam("artID") String artID){
        return Apiresponse.success(articleService.isSecret(artID));
    }

    @ApiOperation("解密文章")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "解密成功"),
            @ApiResponse(code = 500,message = "解密失败")
    })
    @PostMapping("/decrypt")
    public @ResponseBody
    Apiresponse blogDecrypt(@ApiParam("{artId:xx,pwd:xx}") @RequestBody String data){
        JSONObject datalist = JSONObject.parseObject(data);

        String artid = datalist.get("artId").toString();
        String pwd = datalist.get("pwd").toString();
        if (articleService.contentDecrypt(artid,pwd) == null) return Apiresponse.fail();
        return Apiresponse.success(articleService.contentDecrypt(artid,pwd));

    }

}
