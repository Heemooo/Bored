package com.bored.core;

import com.bored.model.Category;
import com.bored.model.Tag;
import lombok.Data;

import java.util.List;

@Data
public class Page {
    /**
     * 标题
     */
    private String title;
    /**
     * 创建时间
     */
    private String date;
    /**
     * url
     */
    private String permLink;
    /**
     * 描述
     */
    private String description;
    /**
     * 内容
     */
    private String content;
    /**
     * 分类
     */
    private List<String> categories;
    /**
     * 标签
     */
    private List<String> tags;
    /**
     * 下一篇
     */
    private Page next;
    /**
     * 上一篇
     */
    private Page prev;

}
