package com.bored.loader;

import com.bored.Bored;
import com.bored.constant.ContentType;
import com.bored.context.Context;
import com.bored.context.DefaultContextFactory;
import com.bored.context.StaticContextFactory;

import java.nio.charset.StandardCharsets;

enum ErrorLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    private static final Context DEFAULT_ERROR;

    private static final String url;

    static {
        var bytes = Bored.CONSTANT.getStr("404.default.content").getBytes(StandardCharsets.UTF_8);
        url = "/error";
        DEFAULT_ERROR = new StaticContextFactory(url, ContentType.TEXT_HTML, bytes).create();
    }

    @Override
    public void loading() {
        if (!Bored.jetTemplateHelper().checkTemplate("error.html")) {
            Bored.url(DEFAULT_ERROR);
        } else {
            var context = new DefaultContextFactory(url, "", "error").create();
            Bored.url(context);
        }
    }
}