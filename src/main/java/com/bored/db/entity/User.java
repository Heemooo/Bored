
package com.bored.db.entity;

import lombok.Data;

@Data
//用户类
public class User {
    private long id;
    private String username;
    private String password;
}