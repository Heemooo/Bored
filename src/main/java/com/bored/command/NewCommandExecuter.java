package com.bored.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.bored.Bored;
import com.bored.model.CompleteEnvironment;
import com.bored.model.Environment;
import com.bored.model.Page;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class NewCommandExecuter implements CommandExecuter {

    @Override
    public void execute(String command, String value) {
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
        var env = new Environment(Bored.of().getRoot());
        Bored.of().setEnv(env);
        String site = Bored.convertCorrectPath(env.getRoot() + "/" + siteName);
        if (FileUtil.exist(site)) {
            log.info("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        ClassPathResource resource = new ClassPathResource("demo/site-template.zip");
        ZipUtil.unzip(resource.getPath(), site);
    }

    private void theme(String name) {
        var env = new CompleteEnvironment(Bored.of().getRoot());
        Bored.of().setEnv(env);
        String themePath = Bored.convertCorrectPath(env.getRoot() + "/themes/" + name);
        ClassPathResource resource = new ClassPathResource("demo/theme-template.zip");
        ZipUtil.unzip(resource.getPath(), themePath);
    }

    private void page(String name) {
        var env = new CompleteEnvironment(Bored.of().getRoot());
        Bored.of().setEnv(env);
        if (name.contains(".md") == Boolean.FALSE) {
            name = name + ".md";
        }
        String filePath = String.format("%s/%s", env.getPagePath(), name);
        var page = new File(filePath);
        if (FileUtil.exist(page)) {
            log.error("Page {} existed!", name);
            return;
        }
        FileUtil.touch(page);
        try {
            String frontMatterPath = env.getFrontMatterPath();
            List<String> archetypeContents = new FileReader(frontMatterPath).readLines();
            StringBuilder templateContent = new StringBuilder(env.getSiteConfig().getFrontMatterSeparator());
            templateContent.append(env.getLineSeparator());
            archetypeContents.forEach(line -> {
                if (!line.startsWith("#") && !line.isBlank()) {
                    templateContent.append(line);
                    templateContent.append(env.getLineSeparator());
                }
            });
            templateContent.append(env.getSiteConfig().getFrontMatterSeparator());
            var frontMatter = new Page.FrontMatter();
            frontMatter.setTitle(StrUtil.removeSuffix(page.getName(), ".md"));
            String content = env.getJetTemplateHelper().parseSource(templateContent.toString(), Bored.objToMap(frontMatter, frontMatter.getClass()));
            @Cleanup FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            log.info("Create file: {}", filePath);
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
