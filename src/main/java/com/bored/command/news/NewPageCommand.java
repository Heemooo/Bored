package com.bored.command.news;

import cn.hutool.core.io.file.FileReader;
import com.bored.core.Bored;
import com.bored.core.DefaultFile;
import com.bored.core.FrontMatter;

public class NewPageCommand extends AbstractNewSomeCommand {

    public NewPageCommand(String path) {
        super(path);
    }

    public NewPageCommand(String path, String name) {
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
