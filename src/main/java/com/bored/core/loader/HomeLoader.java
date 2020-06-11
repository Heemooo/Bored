package com.bored.core.loader;

import cn.hutool.core.collection.CollUtil;
import com.bored.Bored;
import com.bored.core.ContentType;
import com.bored.core.model.Context;
import com.bored.core.model.Page;
import com.bored.util.PaginationUtil;
import com.bored.util.Paths;

import java.util.Date;
import java.util.List;

/**
 * 加载home
 */
enum HomeLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    @Override
    public void loading() {
        List<Page> pages = Bored.pages();
        var paginationList = PaginationUtil.loadPagination(pages, null);
        paginationList.forEach(pagination -> {
            var outPutPath = Paths.outputPath() + "/page/" + pagination.getCurrent() + ".html";
            var context = Context.builder().contentType(ContentType.TEXT_HTML)
                    .title("首页-第" + pagination.getCurrent() + "页")
                    .url(pagination.getUri())
                    .date(new Date())
                    .layout("index.html")
                    .outPutPath(outPutPath)
                    .build()
                    .addObject("pages", pages)
                    .addObject("pagination", pagination);
            Bored.url(context);

        });
        var uri = "/index" + Bored.config().getURLSuffix();
        var outPutPath = Paths.outputPath() + "/index.html";
        var indexContext = Context.builder()
                .contentType(ContentType.TEXT_HTML)
                .title("首页")
                .url(uri)
                .date(new Date())
                .layout("index.html")
                .outPutPath(outPutPath)
                .build()
                .addObject("pages", pages)
                .addObject("pagination", CollUtil.isNotEmpty(paginationList) ? paginationList.get(0) : List.of());
        Bored.url(indexContext);
    }
}