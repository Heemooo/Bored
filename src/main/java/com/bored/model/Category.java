package com.bored.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 */
@Data
public class Category {
    /**
     * 分类名
     */
    private String name;
    /**
     * 分类url
     */
    private String url;
    /**
     * 拥有该分类的文章
     */
    private List<Page> pages = new ArrayList<>();
}
