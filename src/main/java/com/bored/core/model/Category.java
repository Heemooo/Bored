package com.bored.core.model;

import com.bored.core.URL;
import com.bored.core.constant.DefaultTemplate;
import com.bored.util.Paths;
import lombok.Data;

import java.nio.file.Path;
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

    public URL toURL() {
        var title = "分类-" + this.name;
        var context = new Context(title, this.getUrl(), new Date(), DefaultTemplate.CATEGORY_TEMPLATE);
        var outPutPath = String.format(DefaultTemplate.CATEGORY_OUTPUT_FORMAT, Paths.outputPath(), this.name);
        return URL.createHTMLURL(context, outPutPath).add("category", this);
    }

}
