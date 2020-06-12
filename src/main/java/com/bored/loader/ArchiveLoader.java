package com.bored.loader;

import com.bored.Bored;
import com.bored.context.DefaultContextFactory;

import java.util.Date;

enum ArchiveLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    @Override
    public void loading() {
        var url = "/archives";
        var title = "归档:Posts";
        var type = "base";
        var layout = "archive.html";
        var context = new DefaultContextFactory(url, type, layout)
                .create()
                .addObject("title", title)
                .addObject("date", new Date())
                .addObject("pages", Bored.pages());
        Bored.url(context);
    }
}