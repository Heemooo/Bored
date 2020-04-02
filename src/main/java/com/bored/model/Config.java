package com.bored.model;

import cn.hutool.setting.dialect.Props;

public class Config {
    Props props = new Props();

    private Config() {
    }

    private static class PageHolder {
        static Config INSTANCE = new Config();
    }

    public static Config of() {
        return PageHolder.INSTANCE;
    }
}
