package com.bored;

import com.bored.command.Commander;
import com.bored.core.Bored;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] commands) {
        String[] args = {"server", "port", "8080"};
        Bored.execCommandPath = System.getProperty("user.dir") + "/site-demo";
        Commander.parse(args);
    }
}