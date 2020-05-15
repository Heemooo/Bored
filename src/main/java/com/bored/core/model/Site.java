package com.bored.core.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.bored.util.Paths;
import com.moandjiezana.toml.Toml;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Site {

    private String title;

    private String baseURL;

    private String theme = "default";

    private int summaryLength = 200;

    private Boolean enableHtmlSuffix = true;

    private Boolean disableTags = false;

    private Boolean disableCategories = false;

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
            log.error("There's a mistake:{}.", Paths.configPath());
            log.error("Case of error:{}.",e.getCause().getMessage());
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
        var optionalSite = Optional.of(loadTomlFile(Paths.configPath(), Site.class));
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

    /**
     * @param path 配置文件路径
     * @param t    配置文件对应实体类
     * @param <T>  配置文件泛型
     * @return 配置文件实例
     */
    private static <T> T loadTomlFile(String path, Class<T> t) {
        var toml = new Toml();
        var root = Paths.convertCorrectPath(path);
        toml.read(FileUtil.file(root));
        return toml.to(t);
    }
}
