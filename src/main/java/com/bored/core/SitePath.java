package com.bored.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SitePath extends Path {
    @Override
    public SitePath initQueue(String path) {
        return (SitePath) this.addFolder(path + "/" + "archetypes")
                .addFolder(path + "/" + "content")
                .addFolder(path + "/" + "data")
                .addFolder(path + "/" + "static")
                .addFolder(path + "/" + "layouts")
                .addFolder(path + "/" + "themes")
                .addFiles(defaultFile -> defaultFile.setFilePath(path + "/" + "config.toml")
                        .addLine("baseURL = \"http://127.0.0.1/\"")
                        .addLine("languageCode = \"zh-cn\"")
                        .addLine("title = \"My New Hugo Site\""))
                .addFiles(defaultFile -> defaultFile.setFilePath(path + "/" + "archetypes/default.md")
                        .addLine("---").addLine("title: ${name}").addLine("date: ${data}").addLine("draft: true").addLine("---"));
    }
}
