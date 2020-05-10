package com.bored.core;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 当前页面的上下文对象
 * ${this}
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
     * 当前页面模板路径
     */
    private String type;
    /**
     * 当前页面模板名
     */
    private String layout;
    /**
     * 创建文章时间
     */
    private String time;

    public String getTemplatePath() {
        if(Objects.isNull(getType())){
            return getLayout() + ".html";
        }
        return getType() + "/" + getLayout() + ".html";
    }
}
