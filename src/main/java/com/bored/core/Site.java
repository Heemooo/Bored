package com.bored.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.model.Menu;
import com.bored.util.TomlUtil;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

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

    public static Site load(String siteConfigPath) {
        var optionalSite = Optional.of(TomlUtil.loadTomlFile(siteConfigPath, Site.class));
        optionalSite.ifPresent(site -> {
            if (CollUtil.isNotEmpty(site.menus)) {
                Map<String, List<Menu>> menuMap = new HashMap<>();
                site.menus.forEach((name, menus) -> {
                    menus = menus.stream().sorted(Comparator.comparing(Menu::getWeight)).collect(Collectors.toList());
                    menuMap.put(name, menus);
                });
                site.setMenus(menuMap);
            }
        });
        return optionalSite.get();
    }
}
