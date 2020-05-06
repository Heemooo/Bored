package com.bored.db.entity;

import lombok.Data;

/**
 * 分类
 */
@Data
public class Category {
    /**
     * 分类id
     */
    private long id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 分类数量
     */
    private String count;
    /**
     * 分类url
     */
    private String url;

}
