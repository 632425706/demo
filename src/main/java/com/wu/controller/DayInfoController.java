package com.wu.controller;

import com.wu.bean.DayInfo;
import com.wu.bean.UserInfo;
import com.wu.dao.DayInfoDao;
import com.wu.dao.SignInfoDao;
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
    @Autowired
    private DayInfoDao dayInfoDao;
    @Autowired
    private SignInfoService signInfoService;

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
        List<String> signnames = signInfoService.getCurrtName();
        map.put("todayPerson",ListName);
        map.put("todayNum",CountNum);
        map.put("totalCount",signnames.size());
        return map;
    }



    @RequestMapping( "/dailyLog" )
    @ResponseBody
    public Map<String,Object> insertdayinfo(@RequestBody DayInfo dayInfo){
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","插入成功");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datename = sdf.format(date);
        int numm = signInfoService.getCurrtName().size();
        dayInfo.setTotalcheckin(numm);
        String personstr = dayInfo.getSignnamelist().replaceFirst(".*:", "");
        dayInfo.setSignnamelist(personstr);
        try{
            int num = dayInfoDao.selectDayInfoNum(datename);
            if (num==0) {
                dayInfo.setDay(datename);
                dayInfoDao.genDayInfo(dayInfo);
            }else if (num==1){
                dayInfoDao.Updatedayinfo(dayInfo);
            }
        }catch (Exception e){
            map.put("code",1);
            map.put("message","插入或更新失败");
        }
        return map;
    }
    @RequestMapping( "/getDayInfo" )
    @ResponseBody
    public DayInfo getDayInfo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datename = sdf.format(date);
        DayInfo info = dayInfoDao.selectDayInfo(datename);
        return info;
    }
}
