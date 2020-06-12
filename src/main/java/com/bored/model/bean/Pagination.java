package com.bored.model.bean;

import lombok.Data;

import java.util.List;

@Data
public class Pagination {

    private List<Page> data;

    private Boolean hasPrev;

    private String prev;

    private Boolean hasNext;

    private String next;

    private Integer pageCount;

    private Integer current;

    private String uri;

}
