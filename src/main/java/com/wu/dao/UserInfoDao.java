package com.wu.dao;

import com.wu.bean.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者    吴振涛
 * 描述
 * 创建时间 2018年04月22日
 * 任务时间
 * 邮件时间
 */
@Repository
public interface UserInfoDao {
    @Insert("insert into userinfo(name,gender,phone,birthDate,md5Code,solar)" +
            " values(#{name},#{gender},#{phone},#{birthDate},#{md5Code},#{solar})")
    public int genTask(UserInfo userInfo);
    @Select("select count(*) from userinfo where name=#{name} and phone=#{phone}")
    public int selectNameNum(@Param("name") String name, @Param("phone") String phone);
    @Select("select count(*) from userinfo where ID=#{id}")
    public int selectID(@Param("id") String id);
    @Select("select * from userinfo where md5Code=#{md5Code}")
    public List<UserInfo> selectAllByDevice(@Param("md5Code") String md5Code);
    @Select("select * from userinfo where openid=#{openid}")
    public List<UserInfo> selectAllByDevice2(@Param("openid") String openid);
    @Select("select * from userinfo where name=#{name} and phone=#{phone}")
    public UserInfo getPerson(@Param("name") String name, @Param("phone") String phone);
    @Update("update userinfo set gender=#{gender},birthDate=#{birthDate},solar=#{solar} where name=#{name} and phone=#{phone}")
    public void updateInfo(UserInfo userInfo);
    @Update("update userinfo set md5Code=#{md5Code} where name=#{name} and phone=#{phone}")
    public void upDevice(UserInfo userInfo);
    @Select("select name from userinfo where birthDate like #{birthDate1} and xzCode = #{xzCode}")
    public List<String> getBirthdayPerson(@Param("birthDate1") String birthDate1,@Param("xzCode") int xzCode);
    @Select("select name from userinfo where signTime LIKE '${signTime}%' and xzCode = #{xzCode}")
    public List<String> getSign(@Param("signTime") String signTime ,@Param("xzCode") int xzCode);
    @Select("select count(*) from userinfo where signTime LIKE '${signTime}%' and xzCode = #{xzCode}")
    public Long getSignCount(@Param("signTime") String signTime,@Param("xzCode") int xzCode);
    @Select("select count(*) from userinfo ")
    public Long getCount();
    @Select("SELECT * FROM `userinfo` where date_format(signTime,'%y-%m-%d') = date_format(now(),'%y-%m-%d') and xzCode = #{xzCode}")
    public List<UserInfo> getNewPerson(@Param("xzCode") int xzCode);
    @Select("update userinfo set openid=#{openid} where md5Code=#{md5Code}")
    public int updateOpenId(@Param("openid")String openid,@Param("md5Code") String md5Code);
    @Select("update userinfo set md5Code=#{md5Code} where openid=#{openid}")
    public int updateMD5Code(@Param("openid")String openid,@Param("md5Code") String md5Code);
}
