package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.ContentType;
import com.bored.core.context.Context;
import com.bored.core.context.DefaultContextFactory;
import com.bored.core.context.StaticContextFactory;
import com.bored.util.Paths;

import java.nio.charset.StandardCharsets;

enum ErrorLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    private static final Context DEFAULT_ERROR;

    private static final String url;

    private static final String outputPath;

    static {
        var bytes = Bored.CONSTANT.getStr("404.default.content").getBytes(StandardCharsets.UTF_8);
        url = "/error";
        outputPath = Paths.outputPath() + "/error.html";
        DEFAULT_ERROR = new StaticContextFactory(url, ContentType.TEXT_HTML, bytes, outputPath).create();
    }

    @Override
    public void loading() {
        if (!Bored.jetTemplateHelper().checkTemplate("error.html")) {
            Bored.url(DEFAULT_ERROR);
        } else {
            var context = new DefaultContextFactory(url, "", "error", outputPath).create();
            Bored.url(context);
        }
    }
}