package com.yzb.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wangban
 * @date 17:09 2019/2/14
 */
@Getter
@Setter
@ToString
public class User {
    private String username;
    private String password;
    private String nickName;
    private String mobile;
    private Boolean lock;
    private String token;

}
