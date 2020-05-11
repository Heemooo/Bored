package com.bored.core;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 当前页面的上下文对象
 * ${this}
 */
@Builder
public class Context {

    /**
     * 当前页面标题
     */
    public final String title;
    /**
     * 当前页面的url
     */
    public final String url;
    /**
     * 当前页面模板路径
     */
    public final String type;
    /**
     * 当前页面模板名
     */
    public final String layout;
    /**
     * 创建文章时间
     */
    public final String time;

    public String getTemplatePath() {
        if(Objects.isNull(type)){
            return layout + ".html";
        }
        return type + "/" + layout + ".html";
    }
}
