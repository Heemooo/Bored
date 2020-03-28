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
        this.folders.add(Bored.replaceSlash(path + "/" + "archetypes"));
        this.folders.add(Bored.replaceSlash(path + "/" + "content"));
        this.folders.add(Bored.replaceSlash(path + "/" + "data"));
        this.folders.add(Bored.replaceSlash(path + "/" + "layouts"));
        this.folders.add(Bored.replaceSlash(path + "/" + "static"));
        this.folders.add(Bored.replaceSlash(path + "/" + "themes"));
        this.files.add(DefaultFile.init(defaultFile -> {
            defaultFile.setFilePath(Bored.replaceSlash(path + "/" + "config.toml"));
            defaultFile.addLine("baseURL = \"http://127.0.0.1/\"")
                    .addLine("languageCode = \"zh-cn\"")
                    .addLine("title = \"My New Hugo Site\"");

        }));
        return this;
    }


}
