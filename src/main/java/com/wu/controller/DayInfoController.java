package com.wu.controller;

import com.wu.bean.DayInfo;
import com.wu.bean.DeviceInfo;
import com.wu.bean.UserInfo;
import com.wu.dao.DayInfoDao;
import com.wu.dao.DeviceInfoDao;
import com.wu.dao.SignInfoDao;
import com.wu.dao.UserInfoDao;
import com.wu.service.SignInfoService;
import com.wu.service.UserInfoService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private DeviceInfoDao deviceInfoDao;

    @RequestMapping( "/getCurrtPerson" )
    @ResponseBody
    public List<String> getCurrtPerson(String xzCode) throws ParseException {
        int pxzCode =13;
        if(Strings.isNotBlank(xzCode)){
           pxzCode = Integer.parseInt(xzCode);
        }
        return userInfoService.selectCurrentName(pxzCode);
    }
    @RequestMapping( "/getstat" )
    @ResponseBody
    public Map<String,Object> getSignPersions(String xzCode){
        int pxzCode =13;
        if(Strings.isNotBlank(xzCode)){
            pxzCode = Integer.parseInt(xzCode);
        }
        Map<String,Object> map=new HashMap<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String imageupdatename = sdf.format(date);
        List<String> ListName=userInfoDao.getSign(imageupdatename,pxzCode);
        Long CountNum=userInfoDao.getSignCount(imageupdatename,pxzCode);
        List<String> signnames = signInfoService.getCurrtName(pxzCode);
        map.put("todayPerson",ListName);
        map.put("todayNum",CountNum);
        map.put("totalCount",signnames.size());
        return map;
    }

    @RequestMapping("/putOwnStatus")
    @ResponseBody
    public Map<String,Object> putOwnStatus(String openid,String reopenid,String xzCode){
        int ownStatus=deviceInfoDao.selectOwnStatus(openid);
        String ownStatusP = String.valueOf(ownStatus);
        Map<String,Object> res = new TreeMap<>();
        if(ownStatus!=0 && ownStatusP.equals(xzCode+"1")){
            try {
                deviceInfoDao.updateOwnStatus(reopenid,Integer.parseInt(xzCode+"2"));
                res.put("status","200");
                res.put("message","success");
            }catch (Exception e){
                res.put("status","500");
                res.put("message","失误重试");
            }
        }
        res.put("status","400");
        res.put("message","用户无权限");
        return res;
    }

    @RequestMapping("/getOwnStatus")
    @ResponseBody
    public Map<String,Object> getOwnStatus(String openid){
        int ownStatus=deviceInfoDao.selectOwnStatus(openid);
        Map<String,Object> res =new TreeMap<>();
        res.put("status","200");
        res.put("message",ownStatus);
        return res;
    }

    @RequestMapping( "/dailyLog" )
    @ResponseBody
    public Map<String,Object> insertdayinfo(@RequestBody DayInfo dayInfo){
        if(deviceInfoDao.selectOwnStatus(dayInfo.getOpenid())==0){
            Map<String,Object> res=new TreeMap();
            res.put("status","400");
            res.put("message","用户没有相应权限");
            return res;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","插入成功");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datename = sdf.format(date);
        int numm = signInfoService.getCurrtName(dayInfo.getXzCode()).size();
        dayInfo.setTotalcheckin(numm);
        String personstr = dayInfo.getSignnamelist().replaceFirst(".*:", "");
        dayInfo.setSignnamelist(personstr);
        try{
            int num = dayInfoDao.selectDayInfoNum(datename,dayInfo.getXzCode());
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
    public DayInfo getDayInfo(String xzCode) {
        int pxzCode =13;
        if(Strings.isNotBlank(xzCode)){
            pxzCode = Integer.parseInt(xzCode);
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datename = sdf.format(date);
        DayInfo info = dayInfoDao.selectDayInfo(datename,pxzCode);
        return info;
    }
}
