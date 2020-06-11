package com.bored.core.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.bored.Bored;
import com.bored.core.model.Context;
import com.bored.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;

@Slf4j
public class PublishCommand extends Command {

    @Override
    public String outHelp() {
        return "  " + this.getName() + " " + this.getOptionSyntax() + " " + this.getDescription();
    }

    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        Console.log("  <command>   Publish site");
    }

    @Override
    public String getName() {
        return "publish";
    }

    @Override
    public String getDescription() {
        return "Publish site";
    }

    @Override
    public void execute(Deque<String> options) {
        ensureMaxArgumentCount(options, 0);
        ensureMinArgumentCount(options, 0);

        Bored.loadingConfig();
        Bored.loadingFiles();

        FileUtil.del(Paths.outputPath());
        Bored.urls().parallelStream().forEach(Context::out);
        JSONArray json = new JSONArray();
        Bored.pages().forEach(page -> {
            var map = new JSONObject();
            map.put("permLink", page.getPermLink());
            map.put("title", page.getTitle());
            json.add(map);
        });
        FileUtil.writeBytes(json.toString().getBytes(CharsetUtil.CHARSET_UTF_8), Paths.outputPath() + "/pages.json");
    }
}
