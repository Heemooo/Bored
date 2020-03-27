package com.bored.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Console;
import com.bored.core.Bored;
import com.bored.utils.Freemarker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NewCommandHandler implements CommandHandler {

    private String site = "${root}${separator}${siteName}";
    private String archetypes = "${site}${separator}archetypes";
    private String content = "${site}${separator}content";
    private String data = "${site}${separator}data";
    private String layouts = "${site}${separator}layouts";
    private String staticDir = "${site}${separator}static";
    private String themes = "${site}${separator}themes";
    private String config = "${site}${separator}config.toml";

    private final String separator = File.separator;

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
        Map<String, String> params = new HashMap<>();
        params.put("root", root);
        params.put("separator", this.separator);
        params.put("siteName", siteName);
        String site = Freemarker.process(this.site, params);
        if (FileUtil.exist(site)) {
            Bored.out("'{}' 已存在，请删除，或更换网站名 ", siteName);
            return;
        }
        params.put("site", site);
        createFolder(archetypes, params);
        createFolder(content, params);
        createFolder(data, params);
        createFolder(layouts, params);
        createFolder(staticDir, params);
        createFolder(themes, params);
        createFile(config, params);
    }

    private void createFolder(String template, Map<String, String> params) {
        String path = Freemarker.process(template, params);
        Bored.out("Create folder: {}",path);
        FileUtil.mkdir(path);
    }

    private void createFile(String template, Map<String, String> params) {
        String path = Freemarker.process(template, params);
        Bored.out("Create file: {}",path);
        FileUtil.touch(path);
    }

    private void theme(String name) {

    }

    private void page(String name) {

    }
}
