package com.wu.controller;

import com.wu.bean.UserInfo;
import com.wu.bean.UserList;
import com.wu.dao.DeviceInfoDao;
import com.wu.dao.UserInfoDao;
import com.wu.service.UserInfoService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 作者    吴振涛
 * 描述
 * 创建时间 2018年04月22日
 * 任务时间
 * 邮件时间
 */
@Controller
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @RequestMapping( "/genUser" )
    @ResponseBody
    public Map<String, Object> genUser(@RequestBody UserInfo userInfo){
        return userInfoService.insertOne(userInfo.getOpenid(),userInfo.getName(),userInfo.getGender(),userInfo.getPhone(),userInfo.getBirthDate(),userInfo.getCalendar(),userInfo.getNickName(),userInfo.getAvatarUrl(),userInfo.getXzCode());
    }

    @RequestMapping("/syncinfo")
    @ResponseBody
    public Map<String,Object> syncOpenId(String openid,String nickname,String avatarurl){
        return userInfoService.syncInfo(openid,nickname,avatarurl);
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public Map<String,Object> updateUser(String openid,String nickname,String avatarurl){
        return userInfoService.updateUser(openid,nickname,avatarurl);
    }

    //    public Map<String, Object> genUser(String name,String gender,String phone,String birthday,String calender,String nickName,String avatarUrl){
//        return userInfoService.insertOne(name,gender,phone,birthday,calender,nickName,avatarUrl);
//    }
    @RequestMapping( "/getUserList" )
    @ResponseBody
    public List<UserList> getUserList(@RequestBody(required = false) UserInfo userInfo){
        System.out.println(userInfo);
        return userInfoService.selectAllByDevice(userInfo.getOpenid(),userInfo.getNickName(),userInfo.getAvatarUrl());
    }

    @RequestMapping( "/getNesPerson" )
    @ResponseBody
    public List<UserInfo> getNesPerson(int xzCode) throws ParseException {
        return userInfoService.selectNewPerson(xzCode);
    }

}
