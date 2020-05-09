package com.bored.model;

import lombok.Data;

import java.util.List;

@Data
public class Pagination {

    private List<PageFile> data;

    private Boolean hasPrev;

    private String prev;

    private Boolean hasNext;

    private String next;

    private Integer pageCount;

    private Integer current;

    private String templatePath;

    private String uri;

}
