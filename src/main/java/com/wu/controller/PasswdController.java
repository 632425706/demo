package com.wu.controller;

import com.wu.handle.PassData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者    吴振涛
 * 描述
 * 创建时间 2018年06月06日
 * 任务时间
 * 邮件时间
 */
@Controller
public class PasswdController {
    @RequestMapping( "/verifyPwd" )
    @ResponseBody
    public Map<String, Object> checkpasswd(String xxxpwd) throws ParseException {
        Map<String,Object> map=new HashMap<>();
        if (xxxpwd!=null && PassData.passwd.equals(xxxpwd)){
            map.put("code",0);
            map.put("message","密码正确");
            return map;
        }else {
            map.put("code",1);
            map.put("message","密码错误");
            return map;
        }
    }
}
