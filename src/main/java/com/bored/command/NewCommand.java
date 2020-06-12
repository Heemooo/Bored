package com.bored.command;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.bored.Bored;
import com.bored.model.FrontMatter;
import com.bored.model.bean.Site;
import com.bored.util.Paths;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.List;

@Slf4j
public class NewCommand extends Command {

    @Override
    public String outHelp() {
        return "  " + this.getName() + "     " + this.getOptionSyntax() + " " +this.getDescription();
    }

    @Override
    public String getOptionSyntax() {
        return "[site|theme|page] <name>";
    }

    @Override
    public void displayOptionUsage() {
        Console.log("  site  <name>   create a new site");
        Console.log("  theme <name>   create a new theme");
        Console.log("  page  <name>   create a new page");
        Console.log("  <name>  [site,theme,page] name");
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
                log.error("Unknown new option {}", command);
        }
    }

    private void site(String siteName) {
        String sitePath = Paths.site(siteName);
        if (FileUtil.exist(sitePath)) {
            log.error("'{}' name already exists, please delete, or change the site name", siteName);
            return;
        }
        create("template/site-template.zip", new File(sitePath));
        Console.log("Congratulations! Your new Bored site is created in {}. ", sitePath);
    }

    private void theme(String themeName) {
        Site.assertConfigExisted();
        String themePath = Paths.theme(themeName);
        if (FileUtil.exist(themePath)) {
            log.error("'{}' name already exists, please delete, or change the theme name", themeName);
            return;
        }
        create("template/theme-template.zip", new File(themePath));
        Console.log("Creating theme at {}.", themePath);
    }

    @SneakyThrows
    private void create(String fileName, File file) {
        @Cleanup InputStream inputStream = NewCommand.class.getClassLoader().getResourceAsStream(fileName);
        assert inputStream != null;
        ZipUtil.unzip(inputStream, file, CharsetUtil.CHARSET_UTF_8);
    }

    private void page(String name) {
        Bored.loadingConfig();
        if (name.contains(".md") == Boolean.FALSE) {
            name = name + ".md";
        }
        String filePath = Paths.convertCorrectPath(String.format("%s/%s", Paths.pagePath(), name));
        var page = new File(filePath);
        if (FileUtil.exist(page)) {
            log.error("'{}' name already exists, please delete, or change the page name!", name);
            return;
        }
        FileUtil.touch(page);
        try {
            var lineSeparator = System.getProperty("line.separator");
            List<String> archetypeContents = new FileReader(Paths.frontMatterPath(Bored.config().getTheme())).readLines();
            StringBuilder templateContent = new StringBuilder();
            templateContent.append("[^_^]:<>(").append("---").append(")").append(lineSeparator);
            archetypeContents.forEach(line -> {
                /*忽略注释行*/
                if (!line.startsWith("#") && !line.isBlank()) {
                    templateContent.append("[^_^]:<>(").append(line).append(")");
                    templateContent.append(lineSeparator);
                }
            });
            templateContent.append("[^_^]:<>(").append("---").append(")").append(lineSeparator);
            var frontMatter = new FrontMatter();
            frontMatter.setTitle(StrUtil.removeSuffix(page.getName(), ".md"));
            frontMatter.setDate(DateUtil.date());

            String content = Bored.jetTemplateHelper().parseSource(templateContent.toString(), frontMatter.toMap());
            @Cleanup FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            Console.log("{} created", filePath);
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
