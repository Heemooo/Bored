package com.bored.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Site;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResourceUtil {
    private static String root;
    private static Site site;

    public static void init() {
        root = Bored.of().getProps().getStr("root");
        site = Bored.of().getSite();
        Bored.of().setPages(loadPages());
        Bored.of().setStatics(loadStatics());
    }

    private static Map<String, String> loadStatics() {
        var rootPath = Bored.convertCorrectPath(root + "/themes/" + site.getTheme());
        var staticPath = Bored.convertCorrectPath(rootPath + "/static");
        return loading(rootPath,staticPath);
    }


    private static Map<String, Page> loadPages() {
        Map<String, Page> pageMapping = new HashMap<>();
        PageUtil pageUtil = new PageUtil(root, site);
        var pages = pageUtil.parse();
        pages.forEach(page -> page.getUrls().forEach(url -> {
            pageMapping.put(url, page);
            log.info("Mapping {}", url);
        }));
        return pageMapping;
    }

    private static Map<String, String> loading(String root, String path) {
        var resource = new HashMap<String, String>();
        var files = FileUtil.loopFiles(path);
        for (File file : files) {
            var url = Bored.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            resource.put(url, file.getPath());
            log.info("Mapping {}", url);
        }
        return resource;
    }
}
