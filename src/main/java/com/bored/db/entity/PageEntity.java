package com.bored.db.entity;

import cn.schoolwow.quickdao.annotation.Id;
import cn.schoolwow.quickdao.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("page")
public class PageEntity {
    @Id
    private long id;

    private String title;

    private String date;

    private boolean draft;

    private String type = "base";

    private String layout = "page";

    private String url;

    private String permLink;

    private String description;

    private String path;

    private List<String> tags;

    private List<String> categories;

}
