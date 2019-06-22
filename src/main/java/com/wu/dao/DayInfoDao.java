package com.wu.dao;

import com.wu.bean.DayInfo;
import com.wu.bean.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayInfoDao {
    @Insert("insert into dayinfo(dailyLeader,cooker,checker,forwarder,totalcups,photographer,dailylog,propaganda,summingup,logistics,cleaner,environment,day,newpersonslist,totalcheckin,signnamelist,xzCode)" +
            " values(#{dailyLeader},#{cooker},#{checker},#{forwarder},#{totalcups},#{photographer},#{dailylog},#{propaganda},#{summingup},#{logistics},#{cleaner},#{environment},#{day},#{newpersonslist},#{totalcheckin},#{signnamelist},#{xzCode})")
    public int genDayInfo(DayInfo dayInfo);

    @Update("update dayinfo set dailyLeader=#{dailyLeader},cooker=#{cooker},checker=#{checker},forwarder=#{forwarder},totalcups=#{totalcups},photographer=#{photographer},dailylog=#{dailylog}" +
            ",propaganda=#{propaganda},summingup=#{summingup},logistics=#{logistics},cleaner=#{cleaner},environment=#{environment}," +
            "newpersonslist=#{newpersonslist},totalcheckin=#{totalcheckin},signnamelist=#{signnamelist},xzCode=#{xzCode}  where `day`= DATE_FORMAT(NOW(),'%Y-%m-%d')")
    public void Updatedayinfo(DayInfo dayInfo);

    @Select("select count(*) from dayinfo where  day=#{day} and xzCode = #{xzCode}")
    public int selectDayInfoNum( @Param("day") String day ,@Param("xzCode")int xzCode);

    @Select("select * from dayinfo where day=#{day} and xzCode=#{xzCode} limit 0,1")
    public DayInfo selectDayInfo(@Param("day") String day,@Param("xzCode")int xzCode);
}
