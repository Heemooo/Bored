package com.bored.model;

import com.bored.core.Page;
import com.bored.core.Site;
import com.bored.template.JetTemplateHelper;
import lombok.Data;

import java.util.List;

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
     * 文件输出目录
     */
    private String outputPath;
    /**
     * 静态文件输出目录
     */
    private String outputStaticPath;
    /**
     * 配置路径
     */
    private String siteConfigPath;
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
    private List<Page> pages;
    /**
     * 标签列表
     */
    private List<Label> tags;
    /**
     * 分类列表
     */
    private List<Label> categories;
}
