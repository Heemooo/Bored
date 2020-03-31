package com.bored.command.debug;

import cn.hutool.db.Db;
import com.bored.command.CommandHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DebugCommandHandler implements CommandHandler {

    @Override
    public void execute(String command, String value) {
   /*     if(args.length<2){
            log.error("Run 'bored debug enable/disable' for usage.");
        }
        //更新
        String value = args[1];
        String sql = "update config set value = ? where key = 'debug'";
        String enable = "enable";
        String disable = "disable";
        if (value.equals(enable)) {
            Db.use().execute(sql, enable);
            log.info("Debug enable");
            return;
        }
        if (value.equals(disable)) {
            Db.use().execute(sql, disable);
            log.info("Debug disable");
            return;
        }
        log.error("Run 'bored debug enable/disable' for usage.");*/
    }
}
