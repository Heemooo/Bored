package com.bored.core.command;


import cn.hutool.core.util.StrUtil;
import com.bored.server.BoredServer;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.util.Deque;

public class ServerCommand extends Command {

    @Override
    public String getOptionSyntax() {
        return "[port|debug]";
    }

    @Override
    public void displayOptionUsage() {
        println("  port  <portNumber>   启动服务器并指定端口号");
        println("  debug   启用debug模式");
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
        if(options.isEmpty()){
            BoredServer.start(port);
            return;
        }
        String command = options.remove();
        String portStr = StrUtil.EMPTY;
        switch (command) {
            case "debug":
                LogManager.getRootLogger().setLevel(Level.DEBUG);
                break;
            case "port":
                try {
                    if (options.isEmpty()) {
                        printlnError("Port number must be number,but the input is empty");
                        nonError = false;
                        return;
                    }
                    portStr = options.remove();
                    port = Integer.parseInt(portStr);
                } catch (Exception e) {
                    printlnError("Port number must be number,but the input is '{}'", portStr);
                    nonError = false;
                }
                break;
            default:
                printlnError("Unknown server option {}", command);
                nonError = false;
        }
        if (!options.isEmpty()) {
            this.execute(options);
        }
        if ((count == 2 || options.isEmpty()) && nonError) {
            BoredServer.start(port);
        }
    }
}
