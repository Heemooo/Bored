package com.bored.core;

import com.bored.Bored;

import java.io.File;
import java.util.regex.Matcher;

/**
 * 包含所有路径相关的静态方法
 */
public final class Paths {

    private Paths() {
    }

    /**
     * 根路径
     * System.getProperty("user.dir")
     */
    public final static String ROOT = System.getProperty("user.dir") + "/site-demo1";
    /**
     * 主题目录
     */
    public final static String THEME_PATH;
    /**
     * 模板文件目录
     */
    public final static String LAYOUT_PATH;
    /**
     * 静态文件目录
     */
    public final static String STATIC_PATH;
    /**
     * 文章目录
     */
    public final static String PAGE_PATH;
    /**
     * 前辅文路径
     */
    public final static String FRONT_MATTER_PATH;
    /**
     * 配置文件路径
     */
    public final static String CONFIG_PATH;
    /**
     * 文件输出目录
     */
    public final static String OUTPUT_PATH;

    static {
        PAGE_PATH = convertCorrectPath(ROOT + "/content");
        CONFIG_PATH = convertCorrectPath(ROOT + "/config.toml");
        THEME_PATH = convertCorrectPath(ROOT + "/themes/" + Bored.config().getTheme());
        OUTPUT_PATH = convertCorrectPath(ROOT + "/public");
        LAYOUT_PATH = convertCorrectPath(THEME_PATH + "/layouts");
        STATIC_PATH = convertCorrectPath(THEME_PATH + "/static");
        FRONT_MATTER_PATH = convertCorrectPath(THEME_PATH + "/front.matter.toml");

    }

    /**
     * 根据主题名称生成对应的主题路径
     * @param themeName 主题名
     * @return 主题路径
     */
    public static String theme(String themeName) {
        return convertCorrectPath(ROOT + "/themes/" + themeName);
    }

    /**
     * 根据网站名称生成网站路径
     * @param siteName 网站名称
     * @return 网站路径
     */
    public static String site(String siteName) {
        return convertCorrectPath(ROOT + "/" + siteName);
    }

    /**
     * 替换路径中的斜杠为当前系统的斜杠
     * @param path 路径
     * @return 当前系统的路径
     */
    public static String convertCorrectPath(String path) {
        String separator = Matcher.quoteReplacement(File.separator);
        return path.replaceAll("/", separator);
    }
}
