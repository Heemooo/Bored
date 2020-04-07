package com.bored.model;

import com.bored.template.JetTemplateHelper;
import lombok.Data;

@Data
public class Environment {

    public Environment(String root) {
        this.root = root;
    }

    /**
     * 根目录
     */
    private String root;
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
}
