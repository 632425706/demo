package com.wu.service;

import com.wu.bean.DeviceInfo;
import com.wu.bean.UserInfo;
import com.wu.bean.UserList;
import com.wu.dao.DeviceInfoDao;
import com.wu.dao.SignInfoDao;
import com.wu.dao.UserInfoDao;
import com.wu.util.CalendarUtil;
import com.wu.util.ChangeNumToChinese;
import com.wu.util.LunarUtil;
import com.wu.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
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
@Service
public class UserInfoService {
    Logger logger= LoggerFactory.getLogger(UserInfoService.class);
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private SignInfoDao signInfoDao;
    @Autowired
    private DeviceInfoDao deviceInfoDao;
    @Autowired
    private SignInfoService signInfoService;
    private String[] mouths={"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private String[] days={"初一","初二","初三","初四","初五","初六","初七","初八","初九","初十",
            "十一","十二","十三","十四","十五","十六","十七","十八","十九",
            "廿十","廿一","廿二","廿三","廿四","廿五","廿六","廿七","廿八","廿九",
            "三十"};
    @Transactional
    public Map<String,Object> insertOne(String openId,String username, String gender, String phone, String dateValue,String calendar,String nickName,String avatarUrl,int xzCode){
        Map<String,Object> map=new HashMap<>();
        try{
            logger.info(openId+","+username+","+gender+","+phone+","+dateValue+","+calendar);
            String md5Code= MD5Utils.MD5(nickName+avatarUrl);
            DeviceInfo deviceInfo=new DeviceInfo();
            int hasDevice = deviceInfoDao.selectRelationNum(md5Code);
            int has2Device = deviceInfoDao.selectRelationNum2(openId);
            if (hasDevice==0 && has2Device==0){
                deviceInfo.setOpenid(openId);
                deviceInfo.setMd5Code(md5Code);
                deviceInfo.setGrade("1");
                deviceInfo.setNickName(nickName);
                deviceInfo.setAvatarUrl(avatarUrl);
                deviceInfoDao.genDeviceRelation(deviceInfo);
            }
//            插入用户信息------------------------------------
            UserInfo userInfo=new UserInfo();
            userInfo.setMd5Code(md5Code);
            userInfo.setName(username);
            userInfo.setOpenid(openId);
            if ("1".equals(gender)) {
                userInfo.setGender("女");
            }else if ("0".equals(gender)){
                userInfo.setGender("男");
            }
            userInfo.setPhone(phone);
            String birthday="";
            String solar="";
//            ----------------------------------------------
//            1是农历  0是阳历
            try{
                if (calendar.equals("1")){
                    String[] dayStrs = dateValue.split("-");
                    int mouthNum=Integer.parseInt(dayStrs[1]);
                    String mouthStr="";
                    if (mouthNum<10){
                        mouthStr="0"+mouthNum;
                    }else {
                        mouthStr=""+mouthNum;
                    }
                    int dayNum=Integer.parseInt(dayStrs[2]);
                    String dayStrr="";
                    if (dayNum<10){
                        dayStrr="0"+dayNum;
                    }else {
                        dayStrr=""+dayNum;
                    }
//                   Calendar a=Calendar.getInstance();
//                   int year=a.get(Calendar.YEAR);
                    int year=Integer.parseInt(dayStrs[0]);
                    Boolean isRunNain=false;
                    if(year%4==0&&year%100!=0||year%400==0) isRunNain=true;
                    String dayStr=CalendarUtil.lunarToSolar(year+mouthStr+dayStrr,isRunNain);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
                    solar=dateFormat2.format(dateFormat1.parse(dayStr));
                    birthday= ChangeNumToChinese.toChinese(year+"")+"年"+mouths[mouthNum-1]+"月"+days[dayNum-1];
                    userInfo.setSolar(solar);
                }else {
                    String[] dayStrs = dateValue.split("-");

                    int mouthNum=Integer.parseInt(dayStrs[1]);
                    String mouthStr="";
                    if (mouthNum<10){
                        mouthStr="0"+mouthNum;
                    }else {
                        mouthStr=""+mouthNum;
                    }
                    int dayNum=Integer.parseInt(dayStrs[2]);
                    String dayStrr="";
                    if (dayNum<10){
                        dayStrr="0"+dayNum;
                    }else {
                        dayStrr=""+dayNum;
                    }
                    int year=Integer.parseInt(dayStrs[0]);
                    String dayStr = year + mouthStr + dayStrr;
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
                    solar=dateFormat2.format(dateFormat1.parse(dayStr));

                    birthday=(year)+"-"+(mouthStr)+"-"+(dayStrr);

                    userInfo.setSolar(solar);
                }
            }catch (Exception e){
                logger.info("获得日期失败"+dateValue+" "+calendar);
            }


//            ----------------------------------------------
            userInfo.setBirthDate(birthday);
            synchronized (userInfoDao){
                int num = userInfoDao.selectNameNum(username, phone);
                if (num>1){
                    map.put("code",1);
                    map.put("message",username+"信息存在多份明显有误，联系管理员删除");
                    return map;
                }else if(num==1){
                    userInfoDao.updateInfo(userInfo);
                    map.put("code",0);
                    map.put("message",username+"信息更新成功");
                    return map;
                }else {
                    userInfoDao.genTask(userInfo);
                    map.put("code",0);
                    map.put("message",username+"添加信息成功");
                    //插入设备关联信息------------------------------------


                    Map<String, Object> map1 = signInfoService.genSign2(openId,username, phone, nickName, avatarUrl,xzCode);
                    map.putAll(map1);

                    return map;
                }
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            e.fillInStackTrace();
        }

        map.put("code",1);
        map.put("message","插入失败");
        return map;
    }
    public List<UserList> selectAllByDevice(String openid,String nickName, String avatarUrl){
        List<UserList> result=new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        List<String> users=signInfoDao.selectSignId(date);
        String md5Code= MD5Utils.MD5(nickName+avatarUrl);
        List<UserInfo> list = userInfoDao.selectAllByDevice(md5Code);
        if(list == null|| list.size() == 0) {
            list=userInfoDao.selectAllByDevice2(openid);
        }
        for (UserInfo userInfo:list){
            UserList userList1 = new UserList();
            userList1.setId(userInfo.getID());
            userList1.setName(userInfo.getName());
            if (users.contains(userInfo.getID())){
                userList1.setCheck(1);
            }else {
                userList1.setCheck(0);
            }
            result.add(userList1);
        }
        return result;
    }

    public List<String> selectCurrentName(int xzCode) throws ParseException {
        List<String> list=new ArrayList<>();
        String currtDay = LunarUtil.getDayStr();
        String[] allDayStr = currtDay.split("@");

        String[] dayStrs = allDayStr[0].split("-");
        int mouthNum=Integer.parseInt(dayStrs[0]);
        String mouthStr="";
        if (mouthNum<10){
            mouthStr="0"+mouthNum;
        }else {
            mouthStr=""+mouthNum;
        }
        int dayNum=Integer.parseInt(dayStrs[1]);
        String dayStrr="";
        if (dayNum<10){
            dayStrr="0"+dayNum;
        }else {
            dayStrr=""+dayNum;
        }
        String ylStr1 = mouthStr+"-"+dayStrr;
        String ylStr2 = mouthNum+"-"+dayNum;

        list.addAll(userInfoDao.getBirthdayPerson("%"+ylStr1,xzCode));
        list.addAll(userInfoDao.getBirthdayPerson(ylStr2,xzCode));
        list.addAll(userInfoDao.getBirthdayPerson("%-"+ylStr2,xzCode));
        list.addAll(userInfoDao.getBirthdayPerson("%"+allDayStr[1],xzCode));
        return list;
    }

    public List<UserInfo> selectNewPerson(int xzCode) throws ParseException {
        List<UserInfo> list=new ArrayList<>();
        list=userInfoDao.getNewPerson(xzCode);
        return list;
    }

    public Map<String,Object> syncInfo(String openid,String nickname,String avatarUrl){
        Map<String,Object> res=new TreeMap<>();
        try {
            logger.info(openid+","+nickname+","+avatarUrl);
            String md5Code= MD5Utils.MD5(nickname+avatarUrl);
            int hasDevice = deviceInfoDao.selectRelationNum(md5Code);
            if(hasDevice != 0){
                deviceInfoDao.updateOpenId(openid,md5Code);
                userInfoDao.updateOpenId(openid,md5Code);
                res.put("status","200");
                res.put("message","success");
            }else{
                res.put("status","400");
                res.put("message","用户不存在");
            }
        }catch (Exception e){
            res.put("status","500");
            res.put("message","error");
        }
        return res;
    }

    public Map<String,Object> updateUser(String openid,String nickname,String avatarUrl){
        Map<String,Object> res=new TreeMap<>();
        try {
            logger.info(openid+","+nickname+","+avatarUrl);
            String md5Code= MD5Utils.MD5(nickname+avatarUrl);
            int hasDeviceByOpenId = deviceInfoDao.selectRelationNum2(openid);
            int hasDeviceByMD5 = deviceInfoDao.selectRelationNum(md5Code);
            if(hasDeviceByOpenId != 0 && hasDeviceByMD5 == 0) {     // 新老用户 - 存在openId，hasDeviceByMD5=0，说明用户更新了昵称或头像
                deviceInfoDao.updateMD5Code(openid,md5Code,nickname,avatarUrl);
                userInfoDao.updateMD5Code(openid,md5Code);
                res.put("status","200");
                res.put("message","success");
            } else if(hasDeviceByOpenId == 0 && hasDeviceByMD5 != 0) {          // 不存在openId,但是存在MD5Code,说明是老用户，需要更新一下openId
                deviceInfoDao.updateOpenId(openid,md5Code);
                userInfoDao.updateOpenId(openid,md5Code);
                res.put("status","200");
                res.put("message","success");
            } else {
                res.put("status","200");
                res.put("message","无需更新");
            }
        }catch (Exception e){
            res.put("status","500");
            res.put("message","error");
        }
        return res;
    }
}
