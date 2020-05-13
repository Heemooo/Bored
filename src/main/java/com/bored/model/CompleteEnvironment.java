package com.bored.model;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.bored.core.Paths;
import com.bored.core.Site;
import com.bored.template.JetTemplateHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompleteEnvironment extends Environment {

    public CompleteEnvironment() {
        if (!FileUtil.exist(Paths.CONFIG_PATH)) {
            Console.log("Error:Site config.toml not found.");
            Console.log("Error:Maybe should create new site or change directory to the site path.");
            System.exit(0);
        }
        try {
            var site = Site.load(Paths.CONFIG_PATH);
            this.setSiteConfig(site);
        } catch (Exception e) {
            System.exit(0);
        }
        this.setJetTemplateHelper(new JetTemplateHelper(Paths.LAYOUT_PATH));
        jetTemplateConfig(Site.class, "site", this.getSiteConfig());
    }

    public void jetTemplateConfig(Class<?> clazz, String name, Object object) {
        this.getJetTemplateHelper().getEngine().getGlobalContext().set(clazz, name, object);
    }
}
