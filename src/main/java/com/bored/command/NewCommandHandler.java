package com.bored.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import com.bored.core.Bored;
import com.bored.core.SitePath;
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
        String root = Bored.config.getCommandPath();
        String site = Bored.replaceSlash(root + "/" + siteName);
        if (FileUtil.exist(site)) {
            log.info("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        SitePath sitePath = new SitePath().initQueue(site);
        sitePath.createFolder().createFile();
    }

    private void theme(String name) {

    }

    private void page(String name) {

    }
}
