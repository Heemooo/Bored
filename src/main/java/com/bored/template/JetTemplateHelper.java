package com.bored.template;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class JetTemplateHelper {

    private JetEngine engine;

    public JetTemplateHelper(String loadRootPath) {
        log.info("Layout load root path {}", loadRootPath);
        Props props = new Props("jetx.properties");
        props.setProperty("$loader.root", loadRootPath);
        engine = JetEngine.create(props);
    }

    public void addGlobalVariable(Class<?> clazz, String name, Object value) {
        var globalContext = this.engine.getGlobalContext();
        globalContext.set(clazz, name, value);
    }

    public boolean checkTemplate(String templateName) {
        return this.engine.checkTemplate(templateName);
    }

    public String parse(String templateName, Map<String, Object> context) {
        var template = engine.getTemplate(templateName);
        return templateToString(template, context);
    }

    public String parseSource(String source, Map<String, Object> context) {
        var engine = JetEngine.create();
        var template = engine.createTemplate(source);
        return templateToString(template, context);
    }

    public String parseClassPath(String classPath, Map<String, Object> context) {
        var engine = JetEngine.create();
        var template = engine.getTemplate(classPath);
        return templateToString(template, context);
    }

    private String templateToString(JetTemplate template, Map<String, Object> context) {
        try (StringWriter writer = new StringWriter()) {
            template.render(context, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StrUtil.EMPTY;
    }
}
