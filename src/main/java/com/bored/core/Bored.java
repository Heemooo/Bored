package com.bored.core;

import cn.hutool.core.util.CharsetUtil;
import com.bored.command.Commander;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.io.File;
import java.io.StringWriter;
import java.util.regex.Matcher;

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
public final class Bored {

    /**
     * 命令执行路径
     */
    public static String execCommandPath = System.getProperty("user.dir");
    /**
     * 端口
     */
    public static int port = 8000;
    /**
     * 版本号
     */
    public static String version = "v.01.2020.3.31";

    public static void run(String[] commands, String execPath) {
        boolean debug = true;
        //设置为debug模式
        LogManager.getRootLogger().setLevel(Level.DEBUG);
        execCommandPath = execPath;
        Commander.parse(commands);
    }

    /**
     * 解析模板
     * @param template 模板
     * @param params   模板参数
     * @return 完成解析后的模板
     */
    @SneakyThrows
    public static String parseTemplate(String template, Object params) {
        Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_30);
        freemarkerConfig.setDefaultEncoding(CharsetUtil.UTF_8);
        freemarkerConfig.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
        Template tpl = new Template(null, template, freemarkerConfig);
        @Cleanup StringWriter writer = new StringWriter();
        tpl.process(params, writer);
        return writer.toString();
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
}
