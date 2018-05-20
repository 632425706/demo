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
    @Insert("insert into deviceinfo(md5Code,grade,nickName,avatarUrl) values(#{md5Code},#{grade},#{nickName},#{avatarUrl})")
    public int genDeviceRelation(DeviceInfo deviceInfo);
    @Select("select count(*) from deviceinfo where md5Code=#{md5Code} ")
    public int selectRelationNum(@Param("md5Code") String md5Code);
    @Select("select * from deviceinfo ")
    public List<DeviceInfo> selectAll();
}
