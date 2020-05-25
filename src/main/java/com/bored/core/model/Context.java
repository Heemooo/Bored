package com.bored.core.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

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
    /**
     * 模板
     */
    private String template;

    public Context(String url) {
        this.url = url;
    }

    public Context(String title, String url, String layout, Date date) {
        this.title = title;
        this.url = url;
        this.layout = layout;
        this.date = date;
    }

    public Context(String title, String url, String type, String layout, Date date) {
        this.title = title;
        this.url = url;
        this.type = type;
        this.layout = layout;
        this.date = date;
    }

    public Context(String title, String url, Date date, String template) {
        this.title = title;
        this.url = url;
        this.date = date;
        this.template = template;
    }

    public String template() {
        if (Objects.isNull(this.template)) {
            this.template = StrUtil.isBlank(this.getType()) ? this.getLayout() :
                    this.getType() + "/" + this.getLayout();
        }
        return this.template;
    }
}
