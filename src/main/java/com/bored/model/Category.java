package com.bored.model;

import com.bored.util.Paths;
import com.bored.core.URL;
import lombok.Data;

import java.util.ArrayList;
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
        var context = Context.builder().title("分类" + this.getName()).type("base").layout("category").url(this.getUrl()).build();
        return URL.builder().uri(this.getUrl())
                .outPutPath(Paths.outputPath() + "/categories/" + this.getName() + ".html")
                .context(context)
                .contentType("text/html;charset=utf-8").build().add("category", this);
    }

}
