package com.bored;

import com.bored.command.CommandExecute;
import com.bored.model.Environment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
@Setter
@Getter
public final class Bored {
    private Environment env;

    private static class BoredHolder {
        private static Bored INSTANCE = new Bored();
    }

    public static Bored of() {
        return BoredHolder.INSTANCE;
    }


    public static void main(String[] commands) {
        String[] args = {"new", "theme", "test"};
        CommandExecute.start(args);
    }

}
