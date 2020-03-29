package com.bored.core;

import lombok.Data;

import java.util.List;

@Data
public class Page {
    private List<String> header;
    private String content;
}
