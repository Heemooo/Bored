package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.model.Context;
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
        var context = Context.builder().title("归档:Posts")
                .url(url)
                .date(new Date())
                .type("base")
                .layout("archive.html")
                .outPutPath(outPutPath).build();
        context.addObject("pages", Bored.pages());
        Bored.url(context);
    }
}