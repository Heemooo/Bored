package com.bored.db.entity;

import cn.schoolwow.quickdao.annotation.Id;
import cn.schoolwow.quickdao.annotation.TableName;
import lombok.Data;

/**
 * 标签
 */
@Data
@TableName("tag")
public class TagEntity {
    /**
     * 标签id
     */
    @Id
    private long id;
    /**
     * 标签名
     */
    private String name;
    /**
     * 、
     * 标签url
     */
    private String url;
}
