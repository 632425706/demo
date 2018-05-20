package com.wu.service;

import com.wu.bean.DeviceInfo;
import com.wu.bean.SignInfo;
import com.wu.bean.UserInfo;
import com.wu.dao.DeviceInfoDao;
import com.wu.dao.SignInfoDao;
import com.wu.dao.UserInfoDao;
import com.wu.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SignInfoService {
    Logger logger= LoggerFactory.getLogger(SignInfoService.class);
    @Autowired
    private SignInfoDao signInfoDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private DeviceInfoDao deviceInfoDao;
    public Map<String,Object> genSign1(String id,String name){
        Map<String,Object> map=new HashMap<>();
        int ids = userInfoDao.selectID(id);
        if (ids==0){
            map.put("code",1);
            map.put("message","输入用户不存在");
            return map;
        }else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());
            int num = signInfoDao.selectSignNum(id, date);
            if (num == 0) {
                SignInfo signInfo = new SignInfo();
                signInfo.setUserID(id);
                signInfo.setPunchTime(date);
                signInfo.setUserName(name);
                signInfoDao.genSign(signInfo);
                map.put("code",0);
                map.put("message", "签到成功");
                return map;
            } else {
                map.put("code",0);
                map.put("message", "已签到");
                return map;
            }
        }
    }

    public Map<String,Object> genSign2(String name,String phone,String nickName,String avatarUrl){
        Map<String, Object> map=new HashMap<>();
        String md5Code= MD5Utils.MD5(nickName+avatarUrl);
        UserInfo theOne = userInfoDao.getPerson(name, phone);
        if (theOne==null){
            map.put("code",1);
            map.put("message","没有该用户");
            return map;
        }
        theOne.setMd5Code(md5Code);
        userInfoDao.upDevice(theOne);
        int hasDevice = deviceInfoDao.selectRelationNum(md5Code);
        if (hasDevice==0){
            DeviceInfo deviceInfo=new DeviceInfo();
            deviceInfo.setMd5Code(md5Code);
            deviceInfo.setGrade("1");
            deviceInfo.setNickName(nickName);
            deviceInfo.setAvatarUrl(avatarUrl);
            deviceInfoDao.genDeviceRelation(deviceInfo);
        }
        map= genSign1(theOne.getID(),theOne.getName());
        return map;
    }

    public List<String> getCurrtName(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        return signInfoDao.selectSigns(date);
    }
}
