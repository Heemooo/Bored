package com.bored.model;

import com.bored.template.JetTemplateHelper;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Environment {

    /**
     * 根目录
     */
    private String root = System.getProperty("user.dir") + "/site-demo1";
    /**
     * 主题目录
     */
    private String themePath;
    /**
     * 模板文件目录
     */
    private String layoutPath;
    /**
     * 静态文件目录
     */
    private String staticPath;
    /**
     * 文章目录
     */
    private String pagePath;
    /**
     * 前
     */
    private String frontMatterPath;
    /**
     * 网站配置
     */
    private Site siteConfig;
    /**
     * 模板引擎
     */
    private JetTemplateHelper jetTemplateHelper;
    /**
     * 系统换行符
     */
    private String lineSeparator = System.getProperty("line.separator");
    /**
     * 文章列表
     */
    private Map<String, Page> pageMap = new HashMap<>();
    /**
     * 静态资源列表
     */
    private Map<String, String> staticResources = new HashMap<>();

    private Map<String, Tag> tags = new HashMap<>();

    private Map<String, Category> categories = new HashMap<>();
}
