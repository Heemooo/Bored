package com.bored.db.entity;

import lombok.Data;

/**
 * 标签
 */
@Data
public class Tag {
    /**
     * 标签id
     */
    private long id;
    /**
     * 标签名
     */
    private String name;
    /**
     * 标签数量
     */
    private String count;
    /**
     * 、
     * 标签url
     */
    private String url;
}
