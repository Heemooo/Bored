package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.URL;
import com.bored.core.constant.DefaultTemplate;
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
        var uri = "/archives" + Bored.config().getURLSuffix();
        var context = new Context("归档:Posts", uri, new Date(), DefaultTemplate.ARCHIVE_TEMPLATE);
        var outPutPath = Paths.outputPath() + "/archive.html";
        var url = URL.createHTMLURL(context, outPutPath).add("pages", Bored.pages());
        Bored.url(url);
    }
}