package com.bored.loader;

import com.bored.Bored;
import com.bored.context.DefaultContextFactory;
import com.bored.model.bean.Page;
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
            var outputPath = Paths.outputPath() + "/" + type + "/page/" + pagination.getCurrent() + ".html";
            var title = "文章列表";
            var date = new Date();
            var layout = "list.html";
            var context = new DefaultContextFactory(pagination.getUri(), type, layout, outputPath)
                    .create()
                    .addObject("title", title)
                    .addObject("date", date)
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