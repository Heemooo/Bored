package com.bored.model;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.template.JetTemplateHelper;
import com.bored.util.PathUtil;
import com.bored.util.TomlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompleteEnvironment extends Environment {

    public CompleteEnvironment() {
        var siteConfigPath = String.format("%s/config.toml", this.getRoot());
        if (!FileUtil.exist(siteConfigPath)) {
            Console.log("Error:Site config.toml not found.");
            Console.log("Error:Maybe should create new site or change directory to the site path.");
            System.exit(0);
        }
        var site = TomlUtil.loadTomlFile(siteConfigPath, Site.class);
        this.setSiteConfig(site);
        log.debug(site.toString());
        this.setPagePath(PathUtil.convertCorrectPath(String.format("%s/content", this.getRoot())));
        this.setThemePath(PathUtil.convertCorrectPath(String.format("%s/themes/%s", this.getRoot(), site.getTheme())));
        this.setLayoutPath(PathUtil.convertCorrectPath(String.format("%s/layouts", this.getThemePath())));
        this.setJetTemplateHelper(new JetTemplateHelper(this.getLayoutPath()));
        this.getJetTemplateHelper().addGlobalVariable(Site.class, "site", site);

        this.setFrontMatterPath(PathUtil.convertCorrectPath(String.format("%s/front.matter.toml", this.getThemePath())));
        this.setStaticPath(PathUtil.convertCorrectPath(String.format("%s/static", this.getThemePath())));
    }
}
