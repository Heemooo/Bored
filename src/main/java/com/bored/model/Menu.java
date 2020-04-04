package com.bored.model;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private String id;
    private String name;
    private String url;
    private Integer weight;
    private List<Menu> child;
}