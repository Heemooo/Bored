package com.bored.command.server;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class Page {
    private String title;
    @Setter
    private String url;
    private List<String> header;
    private String content;
}
