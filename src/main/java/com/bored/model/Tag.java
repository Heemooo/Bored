package com.bored.model;

import com.bored.util.Paths;
import com.bored.core.URL;
import lombok.Data;

import java.util.ArrayList;
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

    public URL toURL() {
        var context = Context.builder().title("标签" + this.getName()).type("base").layout("tag").url(this.getUrl()).build();
        return URL.builder().uri(this.getUrl())
                .outPutPath(Paths.outputPath() + "/tags/" + this.getName() + ".html")
                .context(context)
                .contentType("text/html;charset=utf-8").build().add("tag", this);
    }
}
