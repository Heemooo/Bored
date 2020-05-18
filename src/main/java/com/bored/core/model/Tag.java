package com.bored.core.model;

import com.bored.core.URL;
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

    public URL toURL() {
        var context = new Context("标签", this.getUrl(), "base", "tag", new Date());
        var outPutPath = Paths.outputPath() + "/tags/" + this.getName() + ".html";
        return URL.createHTMLURL(context, outPutPath).add("tag", this);
    }
}
