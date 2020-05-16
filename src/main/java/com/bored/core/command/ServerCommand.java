package com.bored.core.command;


import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.bored.server.BoredHttpServer;
import com.bored.server.BoredServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.util.Deque;
@Slf4j
public class ServerCommand extends Command {

    @Override
    public String outHelp() {
        return "  " + this.getName() + "  " + this.getOptionSyntax() + " " +this.getDescription();
    }

    @Override
    public String getOptionSyntax() {
        return "[port|debug]";
    }

    @Override
    public void displayOptionUsage() {
        Console.log("  port  <portNumber>   Start the server and specify the port number");
        Console.log("  debug   Enable debug mode");
    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getDescription() {
        return "Start server default port 8000";
    }

    private Integer count = 0;

    private Integer port = 8000;

    private Boolean nonError = true;

    @Override
    public void execute(Deque<String> options) {
        count++;
        ensureMaxArgumentCount(options, 3);
        ensureMinArgumentCount(options, 0);
        String command = options.remove();
        String portStr = StrUtil.EMPTY;
        switch (command) {
            case "debug":
                LogManager.getRootLogger().setLevel(Level.DEBUG);
                break;
            case "port":
                try {
                    if (options.isEmpty()) {
                        log.error("Port number must be number,but the input is empty");
                        nonError = false;
                        return;
                    }
                    portStr = options.remove();
                    port = Integer.parseInt(portStr);
                } catch (Exception e) {
                    log.error("Port number must be number,but the input is '{}'", portStr);
                    nonError = false;
                }
                break;
            default:
                log.error("Unknown server option {}", command);
                nonError = false;
        }
        if (!options.isEmpty()) {
            this.execute(options);
            return;
        }
        if ((count == 2 || options.isEmpty()) && nonError) {
            BoredServer.start(port);
        }
    }
}
