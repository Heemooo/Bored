package com.bored.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Site {

    private String title;

    private String baseURL;

    private String theme;

    private Boolean enableHtmlSuffix;

    private String frontMatterSeparator;

    private Map<String, List<Menu>> menu;

    public String getTheme() {
        return Objects.nonNull(theme) ? theme : "default";
    }

    @Data
    private static class Menu {
        private String id;
        private String name;
        private String url;
        private Integer weight;
        private List<Menu> child;
    }

    private Map<String, Object> params;
}
