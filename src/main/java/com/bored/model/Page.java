package com.bored.model;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class Page {
    @Getter
    private String title;

    @Getter
    private String date;

    @Getter
    private Boolean draft;

    private String type;

    private String layout;

    @Getter
    private List<String> urls = new ArrayList<>();

    @Getter
    private List<String> tags;

    @Getter
    private List<String> categories;

    @Getter
    private String description;

    @Getter
    @Setter
    private String content;

    @Setter
    @Getter
    private List<String> toc;

    @Setter
    @Getter
    private Site site;

    public String getType() {
        return Objects.isNull(type) ? StrUtil.EMPTY : type;
    }

    public String getLayout() {
        return Objects.isNull(type) ? "page" : type;
    }

    public Page(FrontMatter frontMatter) {
        this.title = frontMatter.getTitle();
        this.date = frontMatter.getDate();
        this.draft = frontMatter.getDraft();
        this.type = frontMatter.getType();
        this.layout = frontMatter.getLayout();
        if (StrUtil.isNotBlank(frontMatter.getUrl())) {
            this.urls.add(frontMatter.getUrl());
        }
        this.tags = frontMatter.getTags();
        this.categories = frontMatter.getCategories();
        this.description = frontMatter.getDescription();
    }

    @Data
    public static class FrontMatter {
        public String title = StrUtil.EMPTY;
        public String date = DateUtil.now();
        public Boolean draft = Boolean.TRUE;
        private String url = StrUtil.EMPTY;
        private String type;
        private String layout;
        private List<String> tags;
        private List<String> categories;
        private String description;
    }

}
