package com.bored.core.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Objects;

/**
 * 当前页面的上下文对象
 * ${this}
 */
@Builder
@Getter
public class Context {

    /**
     * 当前页面标题
     */
    private final String title;
    /**
     * 当前页面的url
     */
    public final String uri;
    /**
     * 模板路径
     */
    private final String templatePath;
    /**
     * 创建文章时间
     */
    public final Date date;

    public String uri(){
        return uri;
    }

    public String templatePath() {
        return this.templatePath;
    }
}
