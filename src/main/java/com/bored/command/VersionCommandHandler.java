package com.bored.command;

import cn.hutool.core.lang.Console;
import com.bored.core.Bored;

public class VersionCommandHandler implements CommandHandler {
    @Override
    public void exec(String[] args) {
        Console.log("Bored static site generator {}", Bored.config.getVersion());
    }
}
