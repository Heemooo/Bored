package com.bored.model.bean;

import com.bored.context.Context;
import com.bored.context.DefaultContextFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Tag {
    /**
     * 标签名
     */
    private String name;
    /**
     * 标签url
     */
    private String url;
    /**
     * 拥有该标签的文章
     */
    private List<Page> pages = new ArrayList<>();

    public Tag(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Context toContext() {
        var title = "标签-" + this.name;
        var date = new Date();
        var type = "base";
        var layout = "tag.html";
        return new DefaultContextFactory(url, type, layout)
                .create()
                .addObject("title", title)
                .addObject("date", date)
                .addObject("tag", this);
    }
}
