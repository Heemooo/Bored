package com.bored.core.loader;

import cn.hutool.core.collection.CollUtil;
import com.bored.Bored;
import com.bored.core.URL;
import com.bored.core.constant.DefaultTemplate;
import com.bored.core.model.Context;
import com.bored.core.model.Page;
import com.bored.util.PaginationUtil;
import com.bored.util.Paths;

import java.util.Date;
import java.util.List;

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
            var context = new Context("首页-第" + pagination.getCurrent() + "页", pagination.getUri(), new Date(), DefaultTemplate.HOME_TEMPLATE);
            var outPutPath = Paths.outputPath() + "/page/" + pagination.getCurrent() + ".html";
            var url = URL.createHTMLURL(context, outPutPath)
                    .add("pages", pages)
                    .add("pagination", pagination);
            Bored.url(url);
        });
        var uri = "/index" + Bored.config().getURLSuffix();
        var context = new Context("首页", uri, new Date(), DefaultTemplate.HOME_TEMPLATE);
        var outPutPath = Paths.outputPath() + "/index.html";
        var indexUrl = URL.createHTMLURL(context, outPutPath)
                .add("pages", pages)
                .add("pagination", CollUtil.isNotEmpty(paginationList) ? paginationList.get(0) : List.of());
        Bored.url(indexUrl);
    }
}