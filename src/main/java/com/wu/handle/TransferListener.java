package com.wu.handle;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 作者    吴振涛
 * 描述
 * 创建时间 2018年04月19日
 * 任务时间
 * 邮件时间
 */
@WebListener
public class TransferListener implements ServletContextListener {
    Logger logger= LoggerFactory.getLogger(TransferListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("读取密码");
//        PassData.passwd=(int)((Math.random()*9+1)*100000)+"";
        try {
//            FileUtils.write(new File("passwd.txt"),PassData.passwd);
            List<String> list = FileUtils.readLines(new File("passwd.txt"));
            PassData.passwd=list.get(0);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("写入文件失败"+e.getMessage());
        }
        logger.info("生成成功");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
