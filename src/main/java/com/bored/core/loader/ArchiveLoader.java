package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.context.DefaultContextFactory;
import com.bored.util.Paths;

import java.util.Date;

enum ArchiveLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    @Override
    public void loading() {
        var url = "/archives" + Bored.config().getURLSuffix();
        var outPutPath = Paths.outputPath() + "/archive.html";
        var title = "归档:Posts";
        var type = "base";
        var layout = "archive.html";
        var context = new DefaultContextFactory(url, type, layout, outPutPath)
                .create()
                .addObject("title", title)
                .addObject("date", new Date())
                .addObject("pages", Bored.pages());
        Bored.url(context);
    }
}