package com.bored.core.loader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Loader {
    public static void start() {
        new StaticLoader().statics().pages().tags().categories().archive().list().home()._404();
    }
}
