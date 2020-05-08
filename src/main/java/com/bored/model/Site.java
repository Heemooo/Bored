package com.bored.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Site {

    private String title;

    private String baseURL;

    private String theme = "default";

    private Boolean enableHtmlSuffix = true;

    private Boolean disableTags = false;

    private Boolean disableCategories = false;

    private String frontMatterSeparator = "---";

    private Integer pageSize = 10;

    private Map<String, List<Menu>> menus;

    private Map<String, Object> params;

    public String getURLSuffix() {
        return enableHtmlSuffix ? ".html" : StrUtil.EMPTY;
    }
}
