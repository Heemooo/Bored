package com.bored.core.loader;

import cn.hutool.core.collection.CollUtil;
import com.bored.Bored;
import com.bored.core.context.DefaultContextFactory;
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
        var type = "";
        var layout = "index.html";
        var date = new Date();
        var paginationList = PaginationUtil.loadPagination(pages, null);
        paginationList.forEach(pagination -> {
            var outputPath = Paths.outputPath() + "/page/" + pagination.getCurrent() + ".html";
            var title = "首页-第" + pagination.getCurrent() + "页";
            var context = new DefaultContextFactory(pagination.getUri(), type, layout, outputPath)
                    .create()
                    .addObject("title", title)
                    .addObject("date", date)
                    .addObject("pages", pages)
                    .addObject("pagination", pagination);
            Bored.url(context);
        });
        var url = "/index" + Bored.config().getURLSuffix();
        var outputPath = Paths.outputPath() + "/index.html";
        var title = "首页";
        var context = new DefaultContextFactory(url, type, layout, outputPath)
                .create()
                .addObject("title", title)
                .addObject("date", date)
                .addObject("pages", pages)
                .addObject("pagination", CollUtil.isNotEmpty(paginationList) ? paginationList.get(0) : List.of());
        Bored.url(context);
    }
}