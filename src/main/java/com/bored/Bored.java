package com.bored;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.setting.dialect.Props;
import com.bored.command.Commander;
import com.bored.model.Page;
import com.bored.model.Site;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
@Setter
@Getter
public final class Bored {

    private Bored() {
        String path = System.getProperty("user.dir") + "/site-demo";
        props = new Props();
        props.setProperty("root", path);
    }

    private static class BoredHolder {
        private static Bored INSTANCE = new Bored();
    }

    public static Bored of() {
        return BoredHolder.INSTANCE;
    }

    /**
     * 端口
     */
    private int port = 8000;
    /**
     * 版本号
     */
    private String version = "v.01.2020.3.31";

    private Props props;

    private Site site;

    private Map<String, Page> pages;

    private Map<String, String> statics;

    @SneakyThrows
    public static void main(String[] commands) {
        //String[] args = {"server", "port", "8080"};
        Commander.parse(commands);
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
     * 转换为正确的url
     * @param url url
     * @return 正确的url
     */
    public static String convertCorrectUrl(String url) {
        return url.replaceAll("\\\\", "/");
    }


    public static <T> Map<String, Object> objToMap(Object obj, Class<T> tClass) {
        Field[] fields = ReflectUtil.getFields(tClass);
        Map<String, Object> params = new HashMap<>(fields.length);
        for (Field field : fields) {
            params.put(field.getName(), ReflectUtil.getFieldValue(obj, field));
        }
        return params;
    }
}