package com.bored.command.version;

import cn.hutool.core.lang.Console;
import com.bored.command.Command;
import com.bored.core.Bored;

public class VersionCommand implements Command {
    @Override
    public void exec(String[] args) {
        Console.log("Bored static site generator {}", Bored.VERSION);
    }
}
