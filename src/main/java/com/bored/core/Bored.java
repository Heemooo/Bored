package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.command.Commander;
import com.moandjiezana.toml.Toml;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
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

    public static String parseTemplate(File baseFolder, String template, Object params) {
        try {
            Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_30);
            freemarkerConfig.setDefaultEncoding(CharsetUtil.UTF_8);
            freemarkerConfig.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
            freemarkerConfig.setDirectoryForTemplateLoading(baseFolder);
            Template tpl = freemarkerConfig.getTemplate(template);
            @Cleanup StringWriter writer = new StringWriter();
            tpl.process(params, writer);
            return writer.toString();
        } catch (IOException | TemplateException ignored) {
            log.error(ignored.getMessage());
        }
        return StrUtil.EMPTY;
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

    /**
     * @param path 配置文件路径
     * @param t    配置文件对应实体类
     * @param <T>  配置文件泛型
     * @return 配置文件实例
     */
    public static <T> T loadTomlFile(String path, Class<T> t) {
        var toml = new Toml();
        var root = convertCorrectPath(path);
        toml.read(FileUtil.file(root));
        return toml.to(t);
    }

    public static <T> T tomlToObj(String tomlString, Class<T> tClass) {
        var toml = new Toml();
        toml.read(tomlString);
        return toml.to(tClass);
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
