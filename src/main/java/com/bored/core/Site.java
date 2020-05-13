package com.bored.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
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

    public static Site instance() {
        assertConfigExisted();
        Site site = null;
        try {
            site = Site.load();
        } catch (Exception e) {
            System.exit(0);
        }
        return site;
    }

    public static void assertConfigExisted() {
        if (!FileUtil.exist(Paths.configPath())) {
            Console.log("Site config.toml not found.");
            Console.log("Maybe should create new site or change directory to the site path.");
            System.exit(0);
        }
    }

    private static Site load() {
        var optionalSite = Optional.of(TomlUtil.loadTomlFile(Paths.configPath(), Site.class));
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
