package com.bored.model;

import com.bored.core.Page;
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
