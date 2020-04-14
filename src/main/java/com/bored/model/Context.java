package com.bored.model;

import lombok.Data;

/**
 * 当前页面的上下文对象
 */
@Data
public class Context {
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
     * 分页起始页
     */
    private boolean paginationStart = true;
}
