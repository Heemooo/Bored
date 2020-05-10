package com.bored.core.command;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.bored.Bored;
import com.bored.model.CompleteEnvironment;
import com.bored.model.Environment;
import com.bored.model.FrontMatter;
import com.bored.util.PathUtil;
import lombok.Cleanup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.List;

public class NewCommand extends Command {
    @Override
    public String getOptionSyntax() {
        return "[site|theme|page] <name>";
    }

    @Override
    public void displayOptionUsage() {
        println("  site  <name>   创建一个新网站");
        println("  theme <name>   创建一个主题");
        println("  page  <name>   创建一个页面");
        println("  <name>  网站、主题、页面名称");
    }

    @Override
    public String getName() {
        return "new";
    }

    @Override
    public String getDescription() {
        return "New something";
    }

    @Override
    public void execute(Deque<String> options) {
        ensureMaxArgumentCount(options, 2);
        ensureMinArgumentCount(options, 2);
        String command = options.remove();
        switch (command) {
            case "site":
                site(options.remove());
                break;
            case "theme":
                theme(options.remove());
                break;
            case "page":
                page(options.remove());
                break;
            default:
                printlnError("Unknown new option {}", command);
        }
    }

    private void site(String siteName) {
        var env = new Environment();
        Bored.setEnv(env);
        String site = PathUtil.convertCorrectPath(env.getRoot() + "/" + siteName);
        if (FileUtil.exist(site)) {
            printlnError("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        ClassPathResource resource = new ClassPathResource("template/site-template.zip");
        ZipUtil.unzip(resource.getPath(), site);
        println("Created site {}.", siteName);
    }

    private void theme(String name) {
        var env = new CompleteEnvironment();
        Bored.setEnv(env);
        String themePath = PathUtil.convertCorrectPath(env.getRoot() + "/themes/" + name);
        if (FileUtil.exist(themePath)) {
            printlnError("'{}' 已存在，请删除，或更换主题名 ", name);
            return;
        }
        ClassPathResource resource = new ClassPathResource("template/theme-template.zip");
        ZipUtil.unzip(resource.getPath(), themePath);
    }

    private void page(String name) {
        var env = new CompleteEnvironment();
        Bored.setEnv(env);
        if (name.contains(".md") == Boolean.FALSE) {
            name = name + ".md";
        }
        String filePath = String.format("%s/%s", env.getPagePath(), name);
        var page = new File(filePath);
        if (FileUtil.exist(page)) {
            printlnError("Page {} existed!", name);
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
            var frontMatter = new FrontMatter();
            frontMatter.setTitle(StrUtil.removeSuffix(page.getName(), ".md"));
            frontMatter.setDate(DateUtil.now());

            String content = env.getJetTemplateHelper().parseSource(templateContent.toString(), frontMatter.toMap());
            @Cleanup FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            //log.info("Create file: {}", filePath);
        } catch (IOException e) {
            printlnError("", e);
        }
    }
}
