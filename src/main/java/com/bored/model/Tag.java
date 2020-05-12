package com.bored.model;

import com.bored.core.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
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
    private List<Page> pages = new ArrayList<>();

    public Tag(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
