package com.bored.core;

import cn.hutool.core.lang.Console;
import cn.hutool.setting.dialect.Props;
import com.bored.command.CommandKit;

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
public final class Bored {

    public static Props props = new Props("config.properties");

    public static Config config;

    public static void run(String[] commands) {
        //初始化配置
        config = Config.init(props);
        //解析命令
        CommandKit.init().parse(commands);
    }

    public static void out(String template, Object... values) {
        if (config.isDebug()) {
            Console.log(template, values);
        }
    }

    public static String _404NotFoundContent;

    public final static String CONTENT_TYPE = "text/html; charset=utf-8";

}
