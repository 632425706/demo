package com.wu.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

import java.net.CookieHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Map<String, Object> genSign1(String signInfos)
    {
        Map<String, Object> map = new HashMap();
        //判断时间区间
        String format = "HH:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        String now = sf.format(new Date());
        Date nowTime;
        boolean runFlag=false;
        try {
            nowTime = new SimpleDateFormat(format).parse(now);
            Date startTime = new SimpleDateFormat(format).parse("05:00:00");
            Date endTime = new SimpleDateFormat(format).parse("9:00:00");
            if (isEffectiveDate(nowTime, startTime, endTime)) {
                runFlag = true;
                logger.info("系统时间在早上5点到上午9点之间.");
                map.put("message", "系统时间在早上5点到上午9点之间.");
            } else {
                runFlag = false;
                logger.info("系统时间不在早上5点到上午9点之间.");
                map.put("message", "系统时间不在早上5点到上午9点之间.");
            }
        } catch (java.text.ParseException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!runFlag){
            map.put("code", Integer.valueOf(1));
            return map;
        }

        List<String> success = new ArrayList();
        List<String> hasSign = new ArrayList();
        List<String> fail = new ArrayList();
        Gson gson = new Gson();
        JsonArray jsonArray = null;
        try
        {
            JsonElement jarry = (JsonElement)gson.fromJson(signInfos, JsonElement.class);
            jsonArray = jarry.getAsJsonArray();
        }
        catch (Exception e)
        {
            map.put("code", "1");
            map.put("error", "解析json失败");
            return map;
        }
        for (JsonElement signInfo : jsonArray)
        {
            String id = null;
            String name = null;
            try
            {
                id = signInfo.getAsJsonObject().get("userID").getAsString();
                name = signInfo.getAsJsonObject().get("userName").getAsString();
            }
            catch (Exception e)
            {
                map.put("code", "1");
                map.put("error", "获取id或name失败");
                return map;
            }
            int ids = this.userInfoDao.selectID(id);
            if (ids == 0)
            {
                fail.add(name);
            }
            else
            {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(new Date());
                int num = this.signInfoDao.selectSignNum(id, date);
                if (num == 0)
                {
                    SignInfo signInfo1 = new SignInfo();
                    signInfo1.setUserID(id);
                    signInfo1.setPunchTime(date);
                    signInfo1.setUserName(name);
                    this.signInfoDao.genSign(signInfo1);
                    success.add(name);
                }
                else
                {
                    hasSign.add(name);
                }
            }
        }
        map.put("success", success);
        map.put("hasSign", hasSign);
        map.put("fail", fail);
        map.put("code", Integer.valueOf(0));
        return map;
    }
    public Map<String,Object> genSign2(String name,String phone,String nickName,String avatarUrl){
        Map<String, Object> map=new HashMap<>();




        //判断时间区间
        String format = "HH:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        String now = sf.format(new Date());
        Date nowTime;
        boolean runFlag=false;
        try {
            nowTime = new SimpleDateFormat(format).parse(now);
            Date startTime = new SimpleDateFormat(format).parse("05:00:00");
            Date endTime = new SimpleDateFormat(format).parse("9:00:00");
            if (isEffectiveDate(nowTime, startTime, endTime)) {
                runFlag = true;
                logger.info("系统时间在早上5点到上午9点之间.");
                map.put("SignMessage", "系统时间在早上5点到上午9点之间.");
            } else {
                runFlag = false;
                logger.info("系统时间不在早上5点到上午9点之间.");
                map.put("SignMessage", "系统时间不在早上5点到上午9点之间.");
            }
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!runFlag){
            map.put("Signcode", Integer.valueOf(1));
            return map;
        }



        String md5Code= MD5Utils.MD5(nickName+avatarUrl);
        UserInfo theOne = userInfoDao.getPerson(name, phone);
        if (theOne==null){
            map.put("Signcode",1);
            map.put("SignMessage","没有该用户");
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

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
    //        //判断时间区间
    //        String format = "HH:mm:ss";
    //        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
    //        String now = sf.format(new Date());
    //        Date nowTime;
    //        boolean runFlag=false;
    //        try {
    //            nowTime = new SimpleDateFormat(format).parse(now);
    //            Date startTime = new SimpleDateFormat(format).parse("14:00:00");
    //            Date endTime = new SimpleDateFormat(format).parse("15:00:00");
    //            if (isEffectiveDate(nowTime, startTime, endTime)) {
    //                runFlag = true;
    //            } else {
    //                runFlag = false;
    //            }
    //        } catch (java.text.ParseException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //        System.out.println(runFlag);
    }
}
