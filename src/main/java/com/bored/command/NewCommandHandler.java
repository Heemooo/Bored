package com.bored.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.bored.constant.TemplateResource;
import com.bored.core.Bored;
import com.bored.core.FrontMatter;
import lombok.Cleanup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Slf4j
public class NewCommandHandler implements CommandHandler {

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
        String site = Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/" + siteName);
        if (FileUtil.exist(site)) {
            log.info("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        new NewSitePageCommand(site);
    }

    private void theme(String name) {
        String configToml = Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/config.toml");
        if (FileUtil.exist(configToml) == Boolean.FALSE) {
            log.error("请进入网站根目录,run bored new theme [name].");
            log.error("若网站不存在,请先run bored new site [name]");
            log.error("run cd [name]");
            return;
        }
        String currentPath = Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/themes/" + name);
        new NewThemePageCommand(currentPath, name);
    }

    private void page(String name) {
        String configToml = Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/config.toml");
        if (FileUtil.exist(configToml) == Boolean.FALSE) {
            log.error("请进入网站根目录,run bored new page [name].");
            log.error("若网站不存在,请先run bored new site [name]");
            log.error("run cd [name]");
            return;
        }
        if (name.contains(".md") == Boolean.FALSE) {
            name = name += ".md";
        }
        new NewPagePageCommand(Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/content/" + name));
    }

    @Slf4j
    public abstract static class AbstractNewPageCommand {

        public AbstractNewPageCommand(String path) {
            this.path = path;
            this.initQueue();
            this.create();
        }

        public AbstractNewPageCommand(String path, String name) {
            this.path = path;
            this.name = name;
            this.initQueue();
            this.create();
        }

        public String name;
        public String path;
        private Queue<String> folders = new LinkedList<>();
        private Queue<DefaultFile> files = new LinkedList<>();

        public abstract void initQueue();

        public AbstractNewPageCommand addFolder(String path) {
            this.folders.add(Bored.replaceSlash(path));
            return this;
        }

        public AbstractNewPageCommand addFiles(DefaultFile.DefaultFileInit defaultFileInit) {
            DefaultFile defaultFile = new DefaultFile();
            defaultFileInit.init(defaultFile);
            this.files.add(defaultFile);
            return this;
        }

        private void create() {
            folders.forEach(folder -> {
                log.info("Create folder: {}", folder);
                FileUtil.mkdir(folder);
            });
            files.forEach(file -> {
                log.info("Create file: {}", file.getFilePath());
                FileUtil.touch(file.getFilePath());
                try {
                    @Cleanup FileWriter writer = new FileWriter(file.getFilePath());
                    writer.write(file.getContent());
                } catch (IOException e) {
                    log.error("", e);
                }
            });
        }
    }

    public static class NewSitePageCommand extends AbstractNewPageCommand {

        public NewSitePageCommand(String path) {
            super(path);
        }

        @Override
        public void initQueue() {
            this.addFolder(path + "/" + "content")
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

    public static class NewThemePageCommand extends AbstractNewPageCommand {

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

    public static class NewPagePageCommand extends AbstractNewPageCommand {

        public NewPagePageCommand(String path) {
            super(path);
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

    @Data
    public static class DefaultFile {
        /**
         * 文件全路径
         */
        private String filePath;
        /**
         * 文件内容
         */
        private String content;

        public DefaultFile setFilePath(String filePath) {
            this.filePath = Bored.replaceSlash(filePath);
            return this;
        }

        public interface DefaultFileInit {
            void init(DefaultFile defaultFile);
        }
    }
}
