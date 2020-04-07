package com.bored.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Pagination;
import com.bored.model.Site;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResourceUtil {
    private static String root;
    private static Site site;

    public static Map<String, Page> pages;

    public static Map<String, String> defaultPages;

    public static Map<String, Pagination> paginationMap;

    public static List<Page> pageList;

    public static Map<String, String> statics;

    public static void init() {
        root = Bored.of().getEnv().getRoot();
        site = Bored.of().getEnv().getSiteConfig();
        pages = loadPages();
        defaultPages = loadDefaultPages();
        paginationMap = loadPaginationMap();
        statics = loadStatics();
    }

    private static Map<String, Pagination> loadPaginationMap() {
        var paginationMap = new HashMap<String, Pagination>();
        var pageSize = site.getPageSize();
        var pages = pageList;
        int pageCount = PageUtil.getPageCount(pages, pageSize);
        for (int i = 1; i <= pageCount; i++) {
            var pagination = new Pagination();
            pagination.setCurrent(i);
            pagination.setPageCount(pageCount);
            pagination.setData(PageUtil.startPage(pages, i, pageSize));
            if (i == 1) {
                pagination.setHasPrev(false);
                pagination.setHasNext(true);
                pagination.setNext(getPaginationUrl(i + 1));
            } else if (i == pageCount) {
                pagination.setHasPrev(true);
                pagination.setHasNext(false);
                pagination.setPrev(getPaginationUrl(i - 1));
            } else {
                pagination.setHasPrev(true);
                pagination.setHasNext(true);
                pagination.setPrev(getPaginationUrl(i - 1));
                pagination.setNext(getPaginationUrl(i + 1));
            }
            var url = getPaginationUrl(i);
            log.info("Mapping url {}", url);
            paginationMap.put(url, pagination);
        }
        return paginationMap;
    }

    private static String getPaginationUrl(int pageSize) {
        return site.getEnableHtmlSuffix() ? "/list/" + pageSize + ".html" : "/list/" + pageSize;
    }

    private static Map<String, String> loadDefaultPages() {
        var defaultPages = new HashMap<String, String>();
        if (site.getEnableHtmlSuffix()) {
            defaultPages.put("/list.html", "list.ftl");
        } else {
            defaultPages.put("/list", "list.ftl");
        }
        defaultPages.put("/", "index.ftl");
        return defaultPages;
    }

    private static Map<String, String> loadStatics() {
        var rootPath = Bored.convertCorrectPath(root + "/themes/" + site.getTheme());
        var staticPath = Bored.convertCorrectPath(rootPath + "/static");
        return loading(rootPath, staticPath);
    }


    private static Map<String, Page> loadPages() {
        Map<String, Page> pageMapping = new HashMap<>();
        PageUtil pageUtil = new PageUtil(root, site);
        pageList = pageUtil.parse();
        pageList.forEach(page -> {
            pageMapping.put(page.getPermLink(), page);
            log.info("Mapping {}", page.getPermLink());
            if (StrUtil.isNotBlank(page.getUrl())) {
                pageMapping.put(page.getUrl(), page);
                log.info("Mapping page {}", page.getUrl());
            }
        });
        return pageMapping;
    }

    private static Map<String, String> loading(String root, String path) {
        var resource = new HashMap<String, String>();
        var files = FileUtil.loopFiles(path);
        for (File file : files) {
            var url = Bored.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            resource.put(url, file.getPath());
            log.info("Mapping static resource {}", url);
        }
        return resource;
    }
}
