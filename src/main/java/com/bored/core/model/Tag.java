package com.bored.core.model;

import com.bored.core.constant.DefaultTemplate;
import com.bored.core.context.Context;
import com.bored.core.context.DefaultContextFactory;
import com.bored.util.Paths;
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
        var outPutPath = String.format(DefaultTemplate.TAG_OUTPUT_FORMAT, Paths.outputPath(), this.name);
        var date = new Date();
        var type = "base";
        var layout = "tag.html";
        return new DefaultContextFactory(url, type, layout, outPutPath)
                .create()
                .addObject("title", title)
                .addObject("date", date)
                .addObject("tag", this);
    }
}
