package com.bored.core;

import java.io.File;
import java.util.regex.Matcher;

/**
 * 包含所有路径相关的静态方法
 */
public class Paths {
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
