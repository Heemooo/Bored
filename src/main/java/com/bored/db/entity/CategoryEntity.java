package com.bored.db.entity;

import cn.schoolwow.quickdao.annotation.Id;
import cn.schoolwow.quickdao.annotation.TableName;
import lombok.Data;

/**
 * 分类
 */
@Data
@TableName("category")
public class CategoryEntity {
    /**
     * 分类id
     */
    @Id
    private long id;
    /**
     * 分类名
     */
    private String name;
}
