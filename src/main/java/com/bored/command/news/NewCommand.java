package com.bored.command.news;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.bored.command.Command;
import com.bored.core.Bored;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class NewCommand implements Command {

    private final Map<String, Command> NEW_COMMANDS = new HashMap<>() {{
        put("site", new NewSiteCommand());
        put("theme", new NewThemeCommand());
        put("page", new NewPageCommand());
    }};

    @Override
    public void exec(String[] commands) {
        if (commands.length < 3) {
            FileReader fileReader = new FileReader("/help/zh_cn/new");
            log.error(fileReader.readString());
            return;
        }
        String commandName = commands[1];
        Command command = NEW_COMMANDS.get(commandName);
        if(Objects.nonNull(command)){
            command.exec(commands);
        }
        log.info("Run 'bored new [command] [name]' for usage.");
        String name = commands[2];
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
}
