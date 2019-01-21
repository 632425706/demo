package com.wu.controller;

import com.wu.bean.ResultBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class InsertPicController {
    @Value("${saveFile.path}")
    private String path;
    @Value("${url.path}")
    private String url_path;

    @RequestMapping("/insertPics")
    @ResponseBody
    public ResultBean insertPics(@RequestParam(name = "file") MultipartFile file, @RequestParam(required = true)String openId){
        ResultBean bean = new ResultBean();
        try{
            String fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase()+"."+file.getOriginalFilename().replaceFirst(".*\\.","");
            String savePath = path+ File.separator+openId+File.separator;
            String urlPath = url_path+ File.separator+openId+File.separator+fileName;
            saveFile(file,savePath,fileName);
            Map<String,Object> map = new HashMap<>();
            map.put("url",urlPath);
            bean.makeSuccess(map);
        }catch (Exception e){
            bean.makeFail(e.getMessage());
        }
        return bean;
    }
    /***
     * 保存文件
     * @param file
     * @return
     */
    private void saveFile(MultipartFile file, String path, String fileName) throws IOException {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            File filepath = new File(path);
            if (!filepath.exists())
                filepath.mkdirs();
            // 文件保存路径
            String savePath = path + fileName;
            // 转存文件
            file.transferTo(new File(savePath));
        }
    }
}
