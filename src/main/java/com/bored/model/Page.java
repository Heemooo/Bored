package com.bored.model;

import lombok.Data;

import java.util.List;

@Data
public class Page {

    private String title;

    private String date;

    private boolean draft;

    private String type = "base";

    private String layout = "page";

    private String url;

    private String permLink;

    private String description;

    private String content;

    private List<String> toc;

    private List<String> categories;

    private List<String> tags;
    /**
     * 下一篇
     */
    private Page next;
    /**
     * 上一篇
     */
    private Page prev;

    public String getTemplatePath() {
        return getType() + "/" + getLayout() + ".html";
    }
}

