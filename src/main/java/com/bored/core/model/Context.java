package com.bored.core.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Date;

/**
 * 当前页面的上下文对象
 * ${ctx}
 */
@Data
public class Context {

    /**
     * 当前页面标题
     */
    private String title;
    /**
     * 当前页面的url
     */
    private String url;
    /**
     * 类型，如果前面没有指定，此值将自动派生自目录
     */
    private String type;
    /**
     * 文章模板
     */
    private String layout;
    /**
     * 时间
     */
    private Date date;

    public Context(String url) {
        this.url = url;
    }

    public Context(String title, String url, String type, String layout, Date date) {
        this.title = title;
        this.url = url;
        this.type = type;
        this.layout = layout;
        this.date = date;
    }

    public String template() {
        if (StrUtil.isBlank(this.getType())) {
            return this.getLayout() + ".html";
        }
        return this.getType() + "/" + this.getLayout() + ".html";
    }
}
