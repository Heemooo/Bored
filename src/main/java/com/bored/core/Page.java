package com.bored.core;

import lombok.Data;

import java.util.Date;
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
    private Date date;
    /**
     * page type
     */
    private String type;
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

    @Override
    public String toString() {
        return "Page{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", permLink='" + permLink + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", categories=" + categories +
                ", tags=" + tags +
                '}';
    }
}
