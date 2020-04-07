package com.bored.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Site {

    private String title;

    private String baseURL;

    private String theme = "default";

    private String layoutSuffix = "";

    private Boolean enableHtmlSuffix = true;

    private Boolean disableTags = false;

    private Boolean disableCategories = false;

    private String frontMatterSeparator = "---";

    private Integer pageSize = 10;

    private Map<String, List<Menu>> menus;

    private Map<String, Object> params;


}
