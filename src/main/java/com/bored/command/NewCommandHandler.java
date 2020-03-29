package com.bored.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import com.bored.core.Bored;
import com.bored.core.PagePath;
import com.bored.core.SitePath;
import com.bored.core.ThemePath;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewCommandHandler implements CommandHandler {
    @Override
    public void exec(String[] commands) {
        if (commands.length < 3) {
            ClassPathResource resource = new ClassPathResource("/help/zh_cn/new");
            FileReader fileReader = new FileReader(resource.getPath());
            String result = fileReader.readString();
            Console.log(result);
            Console.log("Run 'bored new [command] [name]' for usage.");
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
        }
    }

    private void site(String siteName) {
        String site = Bored.replaceSlash(Bored.config.getCommandPath() + "/" + siteName);
        if (FileUtil.exist(site)) {
            log.info("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        new SitePath(site);
    }

    private void theme(String name) {
        String configToml = Bored.replaceSlash(Bored.config.getCommandPath() + "/config.toml");
        if (FileUtil.exist(configToml) == Boolean.FALSE) {
            log.error("请进入网站根目录,run bored new theme [name].");
            log.error("若网站不存在,请先run bored new site [name]");
            log.error("run cd [name]");
            return;
        }
        String currentPath = Bored.replaceSlash(Bored.config.getCommandPath() + "/themes/" + name);
        new ThemePath(currentPath,name);
    }

    private void page(String name) {
        String configToml = Bored.replaceSlash(Bored.config.getCommandPath() + "/config.toml");
        if (FileUtil.exist(configToml) == Boolean.FALSE) {
            log.error("请进入网站根目录,run bored new page [name].");
            log.error("若网站不存在,请先run bored new site [name]");
            log.error("run cd [name]");
            return;
        }
        if (name.contains(".md") == Boolean.FALSE) {
            name = name += ".md";
        }
        new PagePath(Bored.replaceSlash(Bored.config.getCommandPath() + "/content/" + name));
    }
}
