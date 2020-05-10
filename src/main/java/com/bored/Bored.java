package com.bored;

import com.bored.core.command.CommandExecute;
import com.bored.model.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
public final class Bored {

    private Environment env;

    private static class BoredHolder {
        private static final Bored INSTANCE = new Bored();
    }

    private static Bored of() {
        return BoredHolder.INSTANCE;
    }

    public static void env(Environment env) {
        Bored.of().env = env;
    }

    public static Environment env() {
        return Bored.of().env;
    }


    public static void main(String[] commands) {
        String[] args = {"server", "port", "8080"};
        CommandExecute.start(args);
    }

}
