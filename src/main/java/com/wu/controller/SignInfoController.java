package com.wu.controller;

import com.wu.service.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class SignInfoController {
    @Autowired
    private SignInfoService signInfoService;
    @RequestMapping( "/signIn1" )
    @ResponseBody
    public Map<String,Object> signIn1(String userID,String userName){
        return signInfoService.genSign1(userID,userName);
    }
    @RequestMapping( "/signIn2" )
    @ResponseBody
    public Map<String,Object> signIn2(String name,String phone,String nickName,String avatarUrl){
        return signInfoService.genSign2(name,phone,nickName,avatarUrl);
    }

    @RequestMapping( "/signNames" )
    @ResponseBody
    public List<String> signNames(){
        return signInfoService.getCurrtName();
    }
}
