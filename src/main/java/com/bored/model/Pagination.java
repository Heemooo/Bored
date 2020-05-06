package com.bored.model;

import com.bored.db.entity.Page;
import lombok.Builder;

import java.util.List;

@Builder
public class Pagination {

    private final List<Page> data;

    private final Boolean hasPrev;

    private final String prev;

    private final Boolean hasNext;

    private final String next;

    private final Integer pageCount;

    private final Integer current;

}
