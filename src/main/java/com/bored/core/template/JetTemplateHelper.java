package com.bored.core.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.bored.core.Variable;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class JetTemplateHelper {

    private final JetEngine engine;

    public JetTemplateHelper(String loadRootPath) {
        assert FileUtil.exist(loadRootPath);
        log.debug("Layout load root path {}", loadRootPath);
        Props props = new Props("jetx.properties");
        props.setProperty("$loader.root", loadRootPath);
        engine = JetEngine.create(props);
    }

    public void globalVariable(String name, Variable variable) {
        this.engine.getGlobalContext().set(Variable.class,name, variable);
    }

    public boolean checkTemplate(String templateName) {
        templateName += ".html";
        return this.engine.checkTemplate(templateName);
    }

    public String parse(String templateName, Map<String, Object> context) {
        boolean templateExisted = checkTemplate(templateName);
        if (templateExisted) {
            var template = engine.getTemplate(templateName);
            return templateToString(template, context);
        }
        log.error("{} template is not found", templateName);
        return StrUtil.EMPTY;
    }

    public String parseSource(String source, Map<String, Object> context) {
        if (StrUtil.isNotBlank(source)) {
            var engine = JetEngine.create();
            var template = engine.createTemplate(source);
            return templateToString(template, context);
        }
        return StrUtil.EMPTY;
    }

    private String templateToString(JetTemplate template, Map<String, Object> context) {
        assert template != null;
        assert context != null;
        try (StringWriter writer = new StringWriter()) {
            template.render(context, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StrUtil.EMPTY;
    }
}
