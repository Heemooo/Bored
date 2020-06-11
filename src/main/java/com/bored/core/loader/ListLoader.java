package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.URL;
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
            var context = new Context("文章列表", pagination.getUri(), type, "list", new Date());
            var outPutPath = Paths.outputPath() + "/" + type + "/page/" + pagination.getCurrent() + ".html";
            var url = URL.createHTMLURL(context, outPutPath)
                    .add("pages", pages)
                    .add("pagination", pagination);
            Bored.url(url);
        });
    }

    @Override
    public void loading() {
        Bored.pageMaps().forEach(ListLoader::loadList);
    }
}