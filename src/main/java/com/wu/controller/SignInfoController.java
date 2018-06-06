package com.wu.controller;

import com.wu.bean.UserInfo;
import com.wu.service.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class SignInfoController {
    @Autowired
    private SignInfoService signInfoService;
    @RequestMapping({"/signIn1"})
    @ResponseBody
    public Map<String, Object> signIn1( String signInfos)
    {
        return this.signInfoService.genSign1(signInfos);
    }
    @RequestMapping( "/signIn2" )
    @ResponseBody
    public Map<String,Object> signIn2(@RequestBody UserInfo userInfo){
        return signInfoService.genSign2(userInfo.getName(),userInfo.getPhone(),userInfo.getNickName(),userInfo.getAvatarUrl());
    }

    @RequestMapping( "/signNames" )
    @ResponseBody
    public List<String> signNames(){
        return signInfoService.getCurrtName();
    }
}
