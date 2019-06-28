package com.wu.dao;

import com.wu.bean.DeviceInfo;
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
 * 创建时间 2018年05月18日
 * 任务时间
 * 邮件时间
 */
@Repository
public interface DeviceInfoDao {
    @Insert("insert into deviceinfo(openid,md5Code,grade,nickName,avatarUrl) values(#{md5Code},#{grade},#{nickName},#{avatarUrl},#{openid})")
    public int genDeviceRelation(DeviceInfo deviceInfo);
    @Update("update deviceinfo set ownStatus = #{ownstatus} where openid=#{openid}")
    public void updateOwnStatus(@Param("openid") String openid,@Param("ownstatus") int ownstatus);
    @Select("select count(*) from deviceinfo where md5Code=#{md5Code} ")
    public int selectRelationNum(@Param("md5Code") String md5Code);
    @Select("select count(*) from deviceinfo where openid=#{openid} ")
    public int selectRelationNum2(@Param("openid")String openid);
    @Select("select * from deviceinfo ")
    public List<DeviceInfo> selectAll();
    @Select("select ownstatus from deviceinfo where openid=#{openid}")
    public int selectOwnStatus(@Param("openid")String openid);
    @Select("update deviceinfo set openid=#{openid} where md5Code=#{md5Code}")
    public int updateOpenId(@Param("openid")String openid,@Param("md5Code") String md5Code);
    @Select("update deviceinfo set md5Code=#{md5Code},nickName=#{nickName},avatarUrl=#{avatarUrl} where openid=#{openid}")
    public int updateMD5Code(@Param("openid")String openid,@Param("md5Code") String md5Code,@Param("nickName") String nickName, @Param("avatarUrl") String avatarUrl);
}
