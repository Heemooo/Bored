package com.bored.command.server;

import lombok.Data;

import java.util.List;

@Data
public class Page {
    private String title;
    private String url;
    private List<String> header;
    private String content;
}
