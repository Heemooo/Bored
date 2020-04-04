package com.bored.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

@Slf4j
public final class TemplateUtil {
    private TemplateUtil() {

    }

    static Configuration CONFIG = new Configuration(Configuration.VERSION_2_3_30);

    static {
        CONFIG.setDefaultEncoding(CharsetUtil.UTF_8);
        CONFIG.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
    }


    /**
     * 解析模板
     * @param template 模板
     * @param params   模板参数
     * @return 完成解析后的模板
     */
    @SneakyThrows
    public static String parseTemplate(String template, Object params) {
        Template tpl = new Template(null, template, CONFIG);
        @Cleanup StringWriter writer = new StringWriter();
        tpl.process(params, writer);
        return writer.toString();
    }

    /**
     * 解析模板
     * @param basePackage 模板根目录
     * @param templateName 模板
     * @param params   模板参数
     * @return 完成解析后的模板
     */
    public static String parseTemplate(String basePackage, String templateName, Object params) {
        try {
            basePackage = Bored.convertCorrectPath(basePackage);
            CONFIG.setDirectoryForTemplateLoading(new File(basePackage));
            var tpl = CONFIG.getTemplate(templateName);
            @Cleanup var writer = new StringWriter();
            tpl.process(params, writer);
            return writer.toString();
        } catch (IOException | TemplateException ignored) {
            log.debug(ignored.getMessage());
        }
        return StrUtil.EMPTY;
    }


}
