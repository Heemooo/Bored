package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.ContentType;
import com.bored.core.model.Context;
import com.bored.util.Paths;

import java.nio.charset.StandardCharsets;
import java.util.Date;

enum ErrorLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    @Override
    public void loading() {
        var url = "/404" + Bored.config().getURLSuffix();
        var outPutPath = Paths.outputPath() + "/404.html";
        var template = "404.html";
        var contextBuilder = Context.builder()
                .url(url)
                .title("404 Not Found")
                .date(new Date())
                .outPutPath(outPutPath)
                .layout(template)
                .contentType(ContentType.TEXT_HTML);
        if (!Bored.jetTemplateHelper().checkTemplate(template)) {
            var bytes = Bored.CONSTANT.getStr("404.default.content").getBytes(StandardCharsets.UTF_8);
            contextBuilder.bytes(bytes);
        }
        Bored.url(contextBuilder.build());
    }
}