package com.wu.controller;

import com.wu.bean.UserInfo;
import com.wu.dao.UserInfoDao;
import com.wu.service.SignInfoService;
import com.wu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
public class DayInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoDao userInfoDao;

    @RequestMapping( "/getCurrtPerson" )
    @ResponseBody
    public List<String> getCurrtPerson() throws ParseException {
        return userInfoService.selectCurrentName();
    }
    @RequestMapping( "/getstat" )
    @ResponseBody
    public Map<String,Object> getSignPersions(){
        Map<String,Object> map=new HashMap<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String imageupdatename = sdf.format(date);
        List<String> ListName=userInfoDao.getSign(imageupdatename);
        Long CountNum=userInfoDao.getSignCount(imageupdatename);
        Long totalCount = userInfoDao.getCount();
        map.put("todayPerson",ListName);
        map.put("todayNum",CountNum);
        map.put("totalCount",totalCount);
        return map;
    }
}
