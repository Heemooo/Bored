package com.bored.core.command;

import cn.hutool.core.io.FileUtil;
import com.bored.Bored;
import com.bored.core.Loader;
import com.bored.util.Paths;
import com.bored.core.model.Site;
import com.bored.core.URL;

import java.util.Deque;

public class PublishCommand extends Command {
    @Override
    public String getOptionSyntax() {
        return "[<command>]";
    }

    @Override
    public void displayOptionUsage() {
        println("  <command>   Publish site");
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
        Bored.config(Site.instance());
        Loader.start();
        FileUtil.del(Paths.outputPath());
        Bored.urls().parallelStream().forEach(URL::out);
    }
}
