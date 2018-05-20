package com.wu.dao;

import com.wu.bean.SignInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignInfoDao {
    @Insert("insert into signinfo(userID,userName,punchTime)" +
            " values(#{userID},#{userName},#{punchTime})")
    public int genSign(SignInfo signInfo);
    @Select("select count(*) from signinfo where userID=#{userID} and punchTime=#{punchTime}")
    public int selectSignNum(@Param("userID") String userID, @Param("punchTime") String punchTime);

    @Select("select userName from signinfo where punchTime=#{punchTime}")
    public List<String> selectSigns(@Param("punchTime") String punchTime);
}
