package com.bored.db.entity;

import cn.hutool.core.util.StrUtil;
import com.bored.db.Column;
import lombok.Data;

import java.sql.Clob;

@Data
public class PageEntity {

    @Column("TITLE")
    private String title;

    @Column("CREATE_TIME")
    private String createTime;

    @Column("DRAFT")
    private Boolean draft;

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

    @Column("TOC")
    private String toc;
}
