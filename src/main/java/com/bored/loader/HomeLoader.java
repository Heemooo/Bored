package com.bored.loader;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.bored.Bored;
import com.bored.context.DefaultContextFactory;
import com.bored.context.StaticContextFactory;
import com.bored.model.bean.Page;
import com.bored.util.PaginationUtil;

import java.nio.charset.StandardCharsets;
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
            var title = "首页-第" + pagination.getCurrent() + "页";
            var context = new DefaultContextFactory(pagination.getUri(), type, layout)
                    .create()
                    .addObject("title", title)
                    .addObject("date", date)
                    .addObject("pages", pages)
                    .addObject("pagination", pagination);
            Bored.url(context);
        });
        var url = "/index";
        var title = "首页";
        var context = new DefaultContextFactory(url, type, layout)
                .create()
                .addObject("title", title)
                .addObject("date", date)
                .addObject("pages", pages)
                .addObject("pagination", CollUtil.isNotEmpty(paginationList) ? paginationList.get(0) : List.of());
        Bored.url(context);

        JSONArray json = new JSONArray();
        Bored.pages().forEach(page -> {
            var map = new JSONObject();
            map.put("permLink", page.getPermLink() + Bored.config().getURLSuffix());
            map.put("title", page.getTitle());
            json.add(map);
        });
        var jsonContext = new StaticContextFactory("/pages.json", "application/json;charset=utf-8", json.toString().getBytes(StandardCharsets.UTF_8)).create();
        Bored.url(jsonContext);
    }
}