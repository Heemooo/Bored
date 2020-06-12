package com.bored.model.bean;

import com.bored.context.Context;
import com.bored.context.DefaultContextFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Category {
    /**
     * 分类名
     */
    private String name;
    /**
     * 分类url
     */
    private String url;
    /**
     * 拥有该分类的文章
     */
    private List<Page> pages = new ArrayList<>();

    public Category(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Context toContext() {
        var title = "分类-" + this.name;
        var type = "base";
        var layout = "category";
        var date = new Date();
        return new DefaultContextFactory(url, type, layout)
                .create()
                .addObject("title", title)
                .addObject("date", date)
                .addObject("category", this);
    }

}
