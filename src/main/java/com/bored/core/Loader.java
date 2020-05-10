package com.bored.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.PageFile;
import com.bored.model.Tag;
import com.bored.util.PathUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Loader {

    public static void start(){
        loadStatics();
        loadPages();
        loadTags();
    }

    public static void loadPages() {
        var env = Bored.env();
        var files = FileUtil.loopFiles(env.getPagePath());
        List<Page> pages = new ArrayList<>();
        for (File file : files) {
            var pageFile = parse(file);
            var page = pageFile.toPage();
            Context context = new Context();
            context.setTime(page.getDate());
            context.setTitle(page.getTitle());
            context.setUrl(page.getPermLink());
            context.setType(pageFile.getFrontMatter().getType());
            context.setLayout(pageFile.getFrontMatter().getLayout());
            URL URL = new URL();
            var fullFilePath = String.format("%s/%s/%s", env.getOutputPath(), context.getType(), pageFile.getHtmlFileName());
            URL.setFullFilePath(fullFilePath);
            URL.setContext(context);
            URL.setUri(context.getUrl());
            URL.add("page", page);
            env.getUrls().put(URL.getUri(), URL);
            pages.add(page);
            log.info("Mapping page {}", URL.getUri());
        }
        env.setPages(pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList()));
        for (int i = 0, len = env.getPages().size(); i < len; i++) {
            if (i < (len - 1)) env.getPages().get(i).setNext(env.getPages().get(i + 1));
            if (i > 0) env.getPages().get(i).setPrev(env.getPages().get(i - 1));
        }
    }

    public static PageFile parse(File file) {
        var site = Bored.env().getSiteConfig();
        var pagePath = Bored.env().getPagePath();
        var filePath = file.getPath();
        var pageFile = new PageFile(file);
        var permLink = StrUtil.removePrefix(filePath, pagePath);
        permLink = PathUtil.convertCorrectUrl(StrUtil.removeSuffix(permLink, ".md") + site.getURLSuffix());
        pageFile.setPermLink(permLink);
        return pageFile;
    }

    private static void loadStatics() {
        var root = PathUtil.convertCorrectPath(Bored.env().getThemePath());
        var path = PathUtil.convertCorrectPath(Bored.env().getStaticPath());
        var files = FileUtil.loopFiles(path);
        for (File file : files) {
            var uri = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
            URL url = new URL();
            url.setFilePath(file.getPath());
            url.setUri(uri);
            url.setContentType(contentType(file.getName(), file.getPath()));
            url.setContext(null);
            url.setFullFilePath(Bored.env().getOutputStaticPath());
            Bored.env().getUrls().put(uri, url);
            log.info("Mapping static resource {}", uri);
        }
    }

    private static String contentType(String fileName, String filePath) {
        if (StrUtil.endWith(fileName, ".css")) {
            return "text/css; charset=utf-8";
        }
        if (StrUtil.endWith(fileName, ".js")) {
            return "application/javascript; charset=utf-8";
        }
        String contentType = FileUtil.getMimeType(filePath);
        if (StrUtil.isEmpty(contentType)) {
            return "application/octet-stream";
        }
        return contentType;
    }

    private static void loadTags(){
        var pages = Bored.env().getPages();
        Map<String, Tag> map = new HashMap<>();
        pages.forEach(page -> {
            var tags = page.getTags();
            if (CollUtil.isNotEmpty(tags)) {
                tags.forEach(tagName -> {
                    var uri = String.format("/tag/%s%s", tagName, Bored.env().getSiteConfig().getURLSuffix());
                    if (map.containsKey(uri)) {
                        map.get(uri).getPages().add(page);
                    } else {
                        Tag tag = new Tag(tagName, uri);
                        tag.getPages().add(page);
                        map.put(uri, tag);
                    }
                });
            }
        });
        map.forEach((uri,tag)->{
            Context context = new Context();
            context.setTitle("分类:" + tag.getName());
            context.setType("base");
            context.setLayout("tag");
            context.setUrl(uri);
            URL url = new URL(uri,Bored.env().getOutputPath() + "/tag/"+tag.getName()+".html",context);
            url.add("tag", tag);
            Bored.env().getUrls().put(uri, url);
            log.info("Mapping tag {}", uri);
        });
        Context context = new Context();
        context.setTitle("分类:Tags");
        context.setType("base");
        context.setLayout("tags");
        var uri = "/tags" + Bored.env().getSiteConfig().getURLSuffix();
        context.setUrl(uri);
        URL url = new URL(uri, Bored.env().getOutputPath() + "/tags.html", context);
        List<Tag> tags = new ArrayList<>(map.values());
        url.add("tags", tags);
        Bored.env().setTags(tags);
        Bored.env().getUrls().put(uri, url);
        log.info("Mapping tag {}", uri);
    }
}
