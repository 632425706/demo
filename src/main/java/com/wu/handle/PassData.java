package com.wu.handle;

import com.wu.service.UserInfoService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


/**
 * 作者    吴振涛
 * 描述    hbase数据抽取
 * 创建时间 2018年04月08日
 * 任务时间
 * 邮件时间
 */
@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
public class PassData {
    public static String passwd;
    Logger logger= LoggerFactory.getLogger(PassData.class);
    //处理一个任务
    public void startTask() {
      passwd=(int)((Math.random()*9+1)*100000)+"";
        try {
            FileUtils.write(new File("passwd.txt"),passwd);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("写入文件失败"+e.getMessage());
        }
    }
}
