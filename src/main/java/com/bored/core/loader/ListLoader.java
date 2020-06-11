package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.ContentType;
import com.bored.core.model.Context;
import com.bored.core.model.Page;
import com.bored.util.Pages;
import com.bored.util.PaginationUtil;
import com.bored.util.Paths;

import java.util.Date;
import java.util.List;

enum ListLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    private static void loadList(String type, List<Page> pageList) {
        List<Page> pages = Pages.sortByDate(pageList);
        var paginationMap = PaginationUtil.loadPagination(pages, type);
        paginationMap.forEach(pagination -> {
            var outPutPath = Paths.outputPath() + "/" + type + "/page/" + pagination.getCurrent() + ".html";
            var context = Context.builder()
                    .title("文章列表")
                    .url(pagination.getUri())
                    .type(type)
                    .layout("list.html")
                    .date(new Date())
                    .outPutPath(outPutPath)
                    .contentType(ContentType.TEXT_HTML)
                    .build()
                    .addObject("pages", pages)
                    .addObject("pagination", pagination);
            Bored.url(context);
        });
    }

    @Override
    public void loading() {
        Bored.pageMaps().forEach(ListLoader::loadList);
    }
}