package com.wu.bean;

import lombok.Data;

/**
 * 作者    吴振涛
 * 描述
 * 创建时间 2018年05月18日
 * 任务时间
 * 邮件时间
 */
@Data
public class DeviceInfo {
    private Long ID;
    private String md5Code;
    private String grade;
    private String nickName;
    private String avatarUrl;
}
