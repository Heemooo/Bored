package com.bored.util;

import cn.hutool.core.util.StrUtil;
import com.bored.Bored;

import java.io.File;
import java.util.regex.Matcher;

/**
 * 包含所有路径相关的静态方法
 */
public final class Paths {
    /**
     * 主题目录
     */
    public static String themePath(String themeName) {
        return convertCorrectPath(Bored.ROOT + "/themes/" + themeName);
    }

    /**
     * 模板文件目录
     */
    public static String layoutPath(String themeName) {
        return convertCorrectPath(themePath(themeName) + "/layouts");
    }

    /**
     * 静态文件目录
     */
    public static String staticPath(String themeName) {
        return convertCorrectPath(themePath(themeName) + "/static");
    }

    /**
     * 文章目录
     */
    public static String pagePath() {
        return convertCorrectPath(Bored.ROOT + "/content");
    }

    /**
     * 前辅文路径
     */
    public static String frontMatterPath(String themeName) {
        return convertCorrectPath(themePath(themeName) + "/front.matter.toml");
    }

    /**
     * 配置文件路径
     */
    public static String configPath() {
        return convertCorrectPath(Bored.ROOT + "/config.toml");
    }

    /**
     * 文件输出目录
     */
    public static String outputPath() {
        return convertCorrectPath(Bored.ROOT + "/public");
    }

    public static String outputPath(String url) {
        return convertCorrectPath(outputPath() + url + ".html");
    }

    /**
     * 根据主题名称生成对应的主题路径
     * @param themeName 主题名
     * @return 主题路径
     */
    public static String theme(String themeName) {
        return convertCorrectPath(Bored.ROOT + "/themes/" + themeName);
    }

    /**
     * 根据网站名称生成网站路径
     * @param siteName 网站名称
     * @return 网站路径
     */
    public static String site(String siteName) {
        return convertCorrectPath(Bored.ROOT + "/" + siteName);
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

    /**
     * 路径换为url
     * @param path 路径
     * @return 正确的url
     */
    public static String toUrl(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * 判断请求路径是否符合后缀设定
     * @param url 请求路径
     * @return 是否符合
     */
    public static boolean checkUrl(String url) {
        return StrUtil.endWith(url, Bored.config().getURLSuffix());
    }

    /**
     * 获取去除后缀的真实 url
     * @param url 请求路径
     * @return 真实 url
     */
    public static String getUrl(String url) {
        return StrUtil.removeSuffix(url, Bored.config().getURLSuffix());
    }
}
