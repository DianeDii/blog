package com.diane.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.diane.blog.util.ApiResponse;
import com.diane.blog.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

/**
 * @author dianedi
 * @date 2020/12/27 7:09
 * @Destription 数据库没有存用户相关，这里只是校检一下，实现登录验证
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("用户登录验证")
    @PostMapping("/login")
    public @ResponseBody ApiResponse login(@RequestBody(required = false) String data){
//        这里用==会比地址，所以得用equals(),他们值相等就行
        JSONObject newArticle = JSONObject.parseObject(data);
        if (newArticle.get("username").toString().equals("diane")  && newArticle.get("pwd").toString().equals("diane")){
            String token = TokenUtil.createToken();
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(10 * 60);
            cookie.setPath("/");
            return ApiResponse.success(cookie);
        }else {
            return ApiResponse.fail("用户名或密码错误！");
        }
    }
}
