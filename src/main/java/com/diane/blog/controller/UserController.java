package com.diane.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.diane.blog.util.Apiresponse;
import com.diane.blog.util.TokenUtil;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import static com.diane.blog.util.ReturnCode.USER_NOT_LOGIN_ERROR;

/**
 * @author dianedi
 * @date 2020/12/27 7:09
 * @Destription 数据库没有存用户相关，这里只是校检一下，实现登录验证
 */
@Api(value = "user_controller" ,tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("用户登录验证")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "登录成功"),
            @ApiResponse(code = 401,message = "用户名或密码错误")
    })
    @PostMapping("/login")
    public @ResponseBody
    Apiresponse login(@ApiParam("用户名密码信息：{'username': xx,'pwd': xx }") @RequestBody(required = false) String data){
//        这里用==会比地址，所以得用equals(),他们值相等就行
        JSONObject logindata = JSONObject.parseObject(data);
        if (logindata.get("username").toString().equals("diane")  && logindata.get("pwd").toString().equals("diane")){
            String token = TokenUtil.createToken();
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(10 * 60);
            cookie.setPath("/");
            return Apiresponse.success(cookie);
        }else {
            return Apiresponse.fail(USER_NOT_LOGIN_ERROR);
        }
    }
}
