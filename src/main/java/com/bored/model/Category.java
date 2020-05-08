package com.bored.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 */
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

    public Category(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Page> getPages() {
        return pages;
    }
}
