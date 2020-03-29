package com.bored.command.news;

import com.bored.constant.TemplateResource;
import com.bored.core.DefaultFile;

public class NewSiteCommand extends AbstractNewSomeCommand {

    public NewSiteCommand(String path) {
        super(path);
    }

    public NewSiteCommand(String path, String name) {
        super(path, name);
    }

    @Override
    public void initQueue() {
        this.addFolder(path + "/" + "archetypes")
                .addFolder(path + "/" + "content")
                .addFolder(path + "/" + "data")
                .addFolder(path + "/" + "static")
                .addFolder(path + "/" + "layouts")
                .addFolder(path + "/" + "themes")
                .addFiles(this::configToml)
                .addFiles(this::archetypesDefaultMd);
    }

    private void configToml(DefaultFile defaultFile) {
        defaultFile.setFilePath(path + "/" + "config.toml").setContent(TemplateResource.SITE_CONFIG_TOML);
    }

    private void archetypesDefaultMd(DefaultFile defaultFile) {
        defaultFile.setFilePath(path + "/" + "archetypes/default.md").setContent(TemplateResource.ARCHETYPES_DEFAULT_MD);
    }
}
