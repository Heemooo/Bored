package com.bored.command.news;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import com.bored.command.Command;
import com.bored.core.Bored;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewCommand implements Command {
    @Override
    public void exec(String[] commands) {
        if (commands.length < 3) {
            ClassPathResource resource = new ClassPathResource("/help/zh_cn/new");
            FileReader fileReader = new FileReader(resource.getPath());
            String result = fileReader.readString();
            log.info(result);
            log.info("Run 'bored new [command] [name]' for usage.");
        }
        String command = commands[1];
        String name = commands[2];
        switch (command) {
            case "site":
                site(name);
                break;
            case "theme":
                theme(name);
                break;
            case "page":
                page(name);
                break;
            default:
                log.info("Run 'bored new [command] [name]' for usage.");
        }
    }

    private void site(String siteName) {
        String site = Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/" + siteName);
        if (FileUtil.exist(site)) {
            log.info("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        new NewSiteCommand(site);
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
        new NewThemeCommand(currentPath,name);
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
        new NewPageCommand(Bored.replaceSlash(Bored.EXEC_COMMAND_PATH + "/content/" + name));
    }
}
