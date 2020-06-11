package com.bored.core.model;

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
     * 类型，如果前面没有指定，此值将自动派生自目录
     */
    private String type;
    /**
     * 文章模板
     */
    private String layout;
    /**
     * url
     */
    private String permLink;
    /**
     * 输出路径
     */
    private String outPutPath;
    /**
     * 摘要
     */
    private String summary;
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
                ", date=" + date +
                ", type='" + type + '\'' +
                ", layout='" + layout + '\'' +
                ", permLink='" + permLink + '\'' +
                ", outPutPath='" + outPutPath + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", categories=" + categories +
                ", tags=" + tags +
                '}';
    }
}
