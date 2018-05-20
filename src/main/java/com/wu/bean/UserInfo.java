package com.wu.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


/**
 * 作者    吴振涛
 * 描述
 * 创建时间 2018年04月22日
 * 任务时间
 * 邮件时间
 */
@Data
public class UserInfo {
        private String ID;
        private String name;
        private String gender;
        private String phone;
        private String birthDate;
        private String calendar;
        private String md5Code;
        private String nickName;
        private String avatarUrl;
}
