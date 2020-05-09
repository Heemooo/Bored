package com.bored.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 */
public class Tag {

    /**
     * 标签名
     */
    private String name;
    /**
     * 标签url
     */
    private String url;
    /**
     * 拥有该标签的文章
     */
    private List<PageFile> pageFiles = new ArrayList<>();

    public Tag(String name, String url) {
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

    public List<PageFile> getPageFiles() {
        return pageFiles;
    }
}
