package com.bored.command.news;

import com.bored.constant.TemplateResource;
import com.bored.core.Bored;
import com.bored.core.DefaultFile;

import java.util.HashMap;
import java.util.Map;

public class NewThemePageCommand extends AbstractNewPageCommand {

    public NewThemePageCommand(String path, String name) {
        super(path, name);
    }

    @Override
    public void initQueue() {
        this.addFolder(path + "/archetypes")
                .addFolder(path + "/layouts")
                .addFolder(path + "/static")
                .addFiles(this::license)
                .addFiles(this::themeToml);
    }

    private void license(DefaultFile defaultFile) {
        defaultFile.setFilePath(path + "/LICENSE").setContent(TemplateResource.THEMES_LICENSE);
    }

    private void themeToml(DefaultFile defaultFile) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("themeName", this.name);
        String content = Bored.parseTemplate(TemplateResource.THEMES_CONFIG_TOML, params);
        defaultFile.setFilePath(path + "/theme.toml").setContent(content);
    }
}
