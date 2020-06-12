package com.bored.model.bean;

import lombok.Data;

@Data
public class Menu {
    private String id;
    private String name;
    private String url;
    private int weight;
}