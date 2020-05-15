package com.bored.core.model;

import com.bored.core.URL;
import com.bored.util.Paths;
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

    public URL toURL() {
        var context = new Context("分类", this.getUrl(), "base", "category", new Date());
        return URL.builder().outPutPath(Paths.outputPath() + "/categories/" + this.getName() + ".html")
                .context(context).contentType("text/html;charset=utf-8").build().add("category", this);
    }

}
