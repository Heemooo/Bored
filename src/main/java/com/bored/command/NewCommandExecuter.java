package com.bored.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.util.TemplateUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class NewCommandExecuter implements CommandExecuter {

    private String root;

    @Override
    public void execute(String command, String value) {
        root = Bored.of().getProps().getStr("root");
        switch (command) {
            case "new site":
                site(value);
                break;
            case "new theme":
                theme(value);
                break;
            case "new page":
                page(value);
                break;
        }
    }

    private void site(String siteName) {
        String site = Bored.convertCorrectPath(root + "/" + siteName);
        if (FileUtil.exist(site)) {
            log.info("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        ClassPathResource resource = new ClassPathResource("demo/site-template.zip");
        ZipUtil.unzip(resource.getPath(), site);
    }

    private void theme(String name) {
        String themePath = Bored.convertCorrectPath(root + "/themes/" + name);
        ClassPathResource resource = new ClassPathResource("demo/theme-template.zip");
        ZipUtil.unzip(resource.getPath(), themePath);
    }

    private void page(String name) {
        if (name.contains(".md") == Boolean.FALSE) {
            name = name + ".md";
        }
        String filePath = String.format("%s/content/%s", root, name);
        var page = new File(filePath);
        if(FileUtil.exist(page)){
            log.error("Page {} existed!",name);
            return;
        }
        FileUtil.touch(page);
        try {
            var site = Bored.of().getSite();
            String archetypesPath = String.format("%s/themes/%s/archetypes/default.toml", root, site.getTheme());
            List<String> archetypeContents = new FileReader(archetypesPath).readLines();
            var lineSeparator = System.getProperty("line.separator");
            StringBuilder templateContent = new StringBuilder(site.getFrontMatterSeparator());
            templateContent.append(lineSeparator);
            archetypeContents.forEach(line -> {
                if (!line.startsWith("#") && !line.isBlank()) {
                    templateContent.append(line);
                    templateContent.append(lineSeparator);
                }
            });
            templateContent.append(site.getFrontMatterSeparator());
            var frontMatter = new Page.FrontMatter();
            frontMatter.setTitle(StrUtil.removeSuffix(page.getName(), ".md"));
            String content = TemplateUtil.parseTemplate(templateContent.toString(), Bored.objToMap(frontMatter, frontMatter.getClass()));
            @Cleanup FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            log.info("Create file: {}", filePath);
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
