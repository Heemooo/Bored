package com.bored.db.entity;

import cn.hutool.core.util.StrUtil;
import com.bored.db.Column;
import lombok.Data;

import java.util.List;

@Data
public class Page {

    @Column("ID")
    private long id;

    @Column("TITLE")
    private String title;

    @Column("DATE")
    private String date;

    @Column("DRAFT")
    private boolean draft;

    @Column("TYPE")
    private String type = StrUtil.EMPTY;

    @Column("LAYOUT")
    private String layout = "page";

    @Column("URL")
    private String url;

    @Column("PERM_LINK")
    private String permLink;

    @Column("DESCRIPTION")
    private String description;

    @Column("CONTENT")
    private String content;

    private List<String> tags;

    private List<String> categories;

    private List<String> toc;

    public Page() {
    }
/*
    public Page(FrontMatter frontMatter) {
        this.title = frontMatter.getTitle();
        this.date = (Objects.isNull(frontMatter.getCreateTime())) ? frontMatter.getDate() : frontMatter.getCreateTime();
        this.draft = frontMatter.getDraft();
        this.type = StrUtil.isNotBlank(frontMatter.getType()) ? frontMatter.getType() : this.type;
        this.layout = StrUtil.isNotBlank(frontMatter.getLayout()) ? frontMatter.getLayout() : this.layout;
        this.url = frontMatter.getUrl();
        this.tags = frontMatter.getTags();
        this.categories = frontMatter.getCategories();
        this.description = frontMatter.getDescription();
    }*/
}

