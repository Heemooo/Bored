package com.bored.model;

import com.bored.core.Site;
import com.bored.core.URL;
import com.bored.server.container.CategoryContainer;
import com.bored.server.container.PageContainer;
import com.bored.server.container.TagContainer;
import com.bored.template.JetTemplateHelper;
import lombok.Data;

import java.util.HashMap;
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
     * 文件输出目录
     */
    private String outputPath;
    /**
     * 静态文件输出目录
     */
    private String outputStaticPath;
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
     * page容器
     */
    private PageContainer pageContainer;
    /**
     * tag容器
     */
    private TagContainer tagContainer;
    /**
     * category容器
     */
    private CategoryContainer categoryContainer;
    /**
     * 静态资源列表
     */
    private Map<String, URL> staticResources = new HashMap<>();
}
