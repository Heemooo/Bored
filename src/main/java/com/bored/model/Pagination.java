package com.bored.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public class Pagination {

    private List<Page> data;

    private Boolean hasPrev;

    private String prev;

    private Boolean hasNext;

    private String next;

    private Integer pageCount;

    private Integer current;

}
