package com.bored.core;

import cn.hutool.setting.dialect.Props;
import lombok.Data;

@Data
public class Config {

    private Config() {
    }

    public static Config init(Props props) {
        PROPS = props;
        return new Config();
    }

    private static Props PROPS;
    /**
     * 命令执行路径
     */
    private String commandPath = System.getProperty("user.dir");
    /**
     * 端口
     */
    private int port = PROPS.getInt("bored.port");
    /**
     * 版本号
     */
    private String version = PROPS.getStr("bored.version");

    /**
     * debug模式
     */
    private boolean debug = true;
}
