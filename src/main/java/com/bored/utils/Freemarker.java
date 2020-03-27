package com.bored.utils;

import cn.hutool.core.util.CharsetUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.StringWriter;

public class Freemarker {

    @SneakyThrows
    public static String process(String templateValue, Object params) {
        Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_30);
        freemarkerConfig.setDefaultEncoding(CharsetUtil.UTF_8);
        freemarkerConfig.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
        Template template = new Template(null, templateValue, freemarkerConfig);
        @Cleanup StringWriter writer = new StringWriter();
        template.process(params, writer);
        return writer.toString();
    }
}
