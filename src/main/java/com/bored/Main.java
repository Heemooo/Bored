package com.bored;

import com.bored.command.Commander;
import com.bored.core.Bored;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] commands) {
        String[] args = {"server", "port", "asdf"};
        Bored.EXEC_COMMAND_PATH = System.getProperty("user.dir") + "/site-demo";
        //设置为debug模式
        LogManager.getRootLogger().setLevel(Level.DEBUG);
        Commander.parse(args);
    }
}