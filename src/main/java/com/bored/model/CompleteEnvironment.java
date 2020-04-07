package com.bored.model;

import cn.hutool.core.io.FileUtil;
import com.bored.Bored;
import com.bored.template.JetTemplateHelper;
import com.bored.util.TomlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompleteEnvironment extends Environment {

    public CompleteEnvironment(String root) {
        super(root);
        var siteConfigPath = String.format("%s/config.toml", this.getRoot());
        if (!FileUtil.exist(siteConfigPath)) {
            log.error("Site config.toml not found.");
            log.error("Maybe should create new site or change directory to the site path.");
            System.exit(0);
        }
        var site = TomlUtil.loadTomlFile(siteConfigPath, Site.class);
        this.setSiteConfig(site);
        log.info(site.toString());
        this.setPagePath(Bored.convertCorrectPath(String.format("%s/content", root)));
        this.setThemePath(Bored.convertCorrectPath(String.format("%s/themes/%s", this.getRoot(), site.getTheme())));
        this.setLayoutPath(Bored.convertCorrectPath(String.format("%s/layouts", this.getThemePath())));
        this.setJetTemplateHelper(new JetTemplateHelper(this.getLayoutPath()));
        this.getJetTemplateHelper().addGlobalVariable(Site.class, "site", site);

        this.setFrontMatterPath(Bored.convertCorrectPath(String.format("%s/front.matter.toml", this.getThemePath())));
        this.setStaticPath(Bored.convertCorrectPath(String.format("%s/static", this.getThemePath())));
    }
}
