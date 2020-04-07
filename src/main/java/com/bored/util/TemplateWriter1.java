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
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class TemplateWriter1 {

    private Map<String,Object> root;

    private Configuration config = new Configuration(Configuration.VERSION_2_3_30);

    public TemplateWriter1() {
        this.root = new HashMap<>();
    }

    public TemplateWriter1 addModel(String key, Object object){
        this.root.put(key, object);
        this.config.setDefaultEncoding(CharsetUtil.UTF_8);
        this.config.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
        return this;
    }

    @SneakyThrows
    public String parseTemplate(String template){
        return parseTemplate(template, this.root);
    }

    /**
     * 解析模板
     * @param template 模板
     * @param params   模板参数
     * @return 完成解析后的模板
     */
    @SneakyThrows
    public String parseTemplate(String template, Object params) {
        Template tpl = new Template(null, template, this.config);
        @Cleanup StringWriter writer = new StringWriter();
        tpl.process(params, writer);
        return writer.toString();
    }

    public String parseTemplate(String basePackage,String templateName){
        return parseTemplate(basePackage, templateName, this.root);
    }

    /**
     * 解析模板
     * @param basePackage  模板根目录
     * @param templateName 模板
     * @param params       模板参数
     * @return 完成解析后的模板
     */
    public  String parseTemplate(String basePackage, String templateName, Object params) {
        try {
            basePackage = Bored.convertCorrectPath(basePackage);
            this.config.setDirectoryForTemplateLoading(new File(basePackage));
            var tpl = this.config.getTemplate(templateName);
            @Cleanup var writer = new StringWriter();
            tpl.process(params, writer);
            return writer.toString();
        } catch (IOException | TemplateException ignored) {
            log.debug(ignored.getMessage());
        }
        return StrUtil.EMPTY;
    }


}
