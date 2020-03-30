package com.bored;

import com.bored.core.Bored;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] commands) {
        if (Objects.isNull(commands) || commands.length <= 0) {
            log.info("Please input command");
            log.info("Run 'bored help' for usage.");
            return;
        }
        Bored.run(commands, System.getProperty("user.dir") + "/site-demo");
    }
}