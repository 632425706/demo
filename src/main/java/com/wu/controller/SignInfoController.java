package com.wu.controller;

import com.wu.bean.UserInfo;
import com.wu.service.SignInfoService;
import org.apache.logging.log4j.util.Strings;
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
    public Map<String, Object> signIn1(String signInfos,String  xzCode)
    {
        int pxzCode =13;
        if(Strings.isNotBlank(xzCode)){
            pxzCode = Integer.parseInt(xzCode);
        }
        return this.signInfoService.genSign1(signInfos,pxzCode);
    }
    @RequestMapping( "/signIn2" )
    @ResponseBody
    public Map<String,Object> signIn2(@RequestBody UserInfo userInfo){
        return signInfoService.genSign2(userInfo.getOpenid(),userInfo.getName(),userInfo.getPhone(),userInfo.getNickName(),userInfo.getAvatarUrl(),userInfo.getXzCode());
    }

    @RequestMapping( "/getDailyList" )
    @ResponseBody
    public List<String> signNames(int xzCode){
        return signInfoService.getCurrtName(xzCode);
    }
}
