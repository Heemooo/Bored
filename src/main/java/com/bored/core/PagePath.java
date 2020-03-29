package com.bored.core;

import cn.hutool.core.io.file.FileReader;

public class PagePath extends Path {

    public PagePath(String path) {
        super(path);
    }

    public PagePath(String path, String name) {
        super(path, name);
    }

    @Override
    public void initQueue() {
        this.addFiles(defaultFile -> {
            defaultFile.setFilePath(this.path);
            loadContent(defaultFile);
        });
    }

    public void loadContent(DefaultFile defaultFile) {
        String templateContent = new FileReader("templates/archetypes/default.md").readString();
        FrontMatter frontMatter = new FrontMatter();
        String content = Bored.parseTemplate(templateContent, frontMatter.toMap());
        defaultFile.setContent(content);
    }
}
