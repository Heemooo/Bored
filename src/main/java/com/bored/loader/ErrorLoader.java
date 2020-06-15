package com.bored.loader;

import com.bored.Bored;
import com.bored.constant.Constants;
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
        var _404 = "404 not found";
        var bytes = _404.getBytes(StandardCharsets.UTF_8);
        url = "/error";
        DEFAULT_ERROR = new StaticContextFactory(url, Constants.CONTENT_TYPE_TEXT_HTML, bytes).create();
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