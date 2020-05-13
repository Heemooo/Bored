package com.bored.model;

import com.bored.core.Paths;
import com.bored.core.Site;
import com.bored.template.JetTemplateHelper;
import lombok.Data;

import java.util.List;

@Data
public class Environment {

    /**
     * 网站配置
     */
    private Site config;
    /**
     * 模板引擎
     */
    private JetTemplateHelper jetTemplateHelper;
    /**
     * 标签列表
     */
    private List<Tag> tags;
    /**
     * 分类列表
     */
    private List<Category> categories;

    public Environment() {
        var site = Site.instance();
        this.setJetTemplateHelper(new JetTemplateHelper(Paths.LAYOUT_PATH));
        this.setConfig(site);
        jetTemplateConfig(Site.class, "site", site);
    }

    public void jetTemplateConfig(Class<?> clazz, String name, Object object) {
        this.getJetTemplateHelper().getEngine().getGlobalContext().set(clazz, name, object);
    }
}
