package com.bored.core;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.dialect.Props;
import com.bored.utils.CommandKit;
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

    public static Props props = new Props("config.properties");

    public static Config config;


    public static String _404NotFoundContent;

    public final static String CONTENT_TYPE = "text/html; charset=utf-8";

    public static void run(String[] commands) {
        //初始化配置
        config = Config.init(props);
        if (config.isDebug()) {
            LogManager.getRootLogger().setLevel(Level.DEBUG);
        }
        //解析命令
        //TODO DEBUG
        config.setCommandPath(config.getCommandPath()+"/site-demo");
        CommandKit.init().parse(commands);
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
    public static String replaceSlash(String path) {
        String separator = Matcher.quoteReplacement(File.separator);
        return path.replaceAll("/", separator);
    }

}
