package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.URL;
import com.bored.core.constant.DefaultTemplate;
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
        var uri = "/404" + Bored.config().getURLSuffix();
        var outPutPath = Paths.outputPath() + "/404.html";
        var context = new Context("404 Not Found", uri, new Date(), DefaultTemplate._404_TEMPLATE);
        if (Bored.jetTemplateHelper().checkTemplate(context.template())) {
            var url = URL.createHTMLURL(context, outPutPath);
            Bored.url(url);
        } else {
            var bytes = Bored.CONSTANT.getStr("404.default.content").getBytes(StandardCharsets.UTF_8);
            var url = URL.createDefaultURL(context, outPutPath, bytes);
            Bored.url(url);
        }
    }
}