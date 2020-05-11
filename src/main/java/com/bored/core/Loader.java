package com.bored.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Label;
import com.bored.model.PageFile;
import com.bored.model.Pagination;
import com.bored.util.PaginationUtil;
import com.bored.util.PathUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Loader {

    private static final String TEXT_HTML = "text/html;charset=utf-8";

    public static void start() {
        loadStatics();
        loadPages();
        loadTags(false);
        loadTags(true);
        loadArchive();
        loadList();
    }

    public static void loadPages() {
        var env = Bored.env();
        var files = FileUtil.loopFiles(env.getPagePath());
        List<Page> pages = new ArrayList<>();
        for (File file : files) {
            var pageFile = parse(file);
            var page = pageFile.toPage();
            var context = Context.builder()
                    .time(page.getDate()).title(page.getTitle()).url(page.getPermLink()).type(pageFile.getFrontMatter().getType())
                    .layout(pageFile.getFrontMatter().getLayout()).build();
            var url = URL.builder()
                    .fullFilePath(String.format("%s/%s/%s", env.getOutputPath(), context.type, pageFile.getHtmlFileName()))
                    .uri(context.url)
                    .context(context)
                    .contentType(TEXT_HTML)
                    .build().add("page", page);
            env.getUrls().put(url.uri, url);
            pages.add(page);
            log.info("Mapping page {}", url.uri);
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
            //TODO 输出完整路径 有问题
            var url = URL.builder().filePath(file.getPath()).uri(uri).contentType(contentType(file.getName(), file.getPath()))
                    .context(null).fullFilePath(Bored.env().getOutputPath() + "").build();
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

    private static void loadTags(boolean isCategory) {
        var name = isCategory ? "category" : "tag";
        var names = isCategory ? "categories" : "tags";
        var title = isCategory ? "分类" : "标签";
        var titles = isCategory ? "分类:Categories" : "标签:Tags";
        var pages = Bored.env().getPages();
        Map<String, Label> map = new HashMap<>();
        pages.forEach(page -> {
            var tags = isCategory ? page.getCategories() : page.getTags();
            if (CollUtil.isNotEmpty(tags)) {
                tags.forEach(tagName -> {
                    var uri = "/" + name + "/" + tagName + Bored.env().getSiteConfig().getURLSuffix();
                    if (map.containsKey(uri)) {
                        map.get(uri).getPages().add(page);
                    } else {
                        Label tag = new Label(tagName, uri);
                        tag.getPages().add(page);
                        map.put(uri, tag);
                    }
                });
            }
        });
        map.forEach((uri, tag) -> {
            var context = Context.builder().title(title + ":" + tag.getName()).type("base").layout(name).url(uri).build();
            var url = URL.builder().uri(uri)
                    .fullFilePath(Bored.env().getOutputPath() + "/" + name + "/" + tag.getName() + ".html")
                    .context(context)
                    .contentType(TEXT_HTML).build()
                    .add(name, tag);
            Bored.env().getUrls().put(uri, url);
            log.info("Mapping {} {}", name, uri);
        });
        var uri = "/" + names + Bored.env().getSiteConfig().getURLSuffix();
        List<Label> tags = new ArrayList<>(map.values());
        var context = Context.builder().title(titles).type("base").layout(names).url(uri).build();
        var url = URL.builder().uri(uri)
                .fullFilePath(Bored.env().getOutputPath() + "/" + names + ".html")
                .context(context)
                .contentType(TEXT_HTML).build().add(names, tags);
        if (isCategory) {
            Bored.env().setCategories(tags);
        } else {
            Bored.env().setTags(tags);
        }
        Bored.env().getUrls().put(uri, url);
        log.info("Mapping {} {}", names, uri);
    }

    private static void loadArchive() {
        var uri = "/archive/posts" + Bored.env().getSiteConfig().getURLSuffix();
        var context = Context.builder().title("归档:Posts").type("post").layout("archive").url(uri).build();
        var url = URL.builder().uri(uri).context(context).contentType(TEXT_HTML).fullFilePath(Bored.env().getOutputPath() + "/archive/posts.html").build()
                .add("pages", Bored.env().getPages());
        Bored.env().getUrls().put(uri, url);
        log.info("Mapping archive {}", uri);
    }

    public static void loadIndex(Pagination pagination) {
        var uri = "/index" + Bored.env().getSiteConfig().getURLSuffix();
        var context = Context.builder().title("首页").layout("index").url(uri).build();
        var url = URL.builder().uri(uri).context(context).fullFilePath(Bored.env().getOutputPath() + "/index.html").contentType(TEXT_HTML).build()
                .add("pages", Bored.env().getPages())
                .add("pagination", pagination);
        Bored.env().getUrls().put(uri, url);
        log.info("Mapping archive {}", uri);
    }

    public static void loadList() {
        var uri = "/posts" + Bored.env().getSiteConfig().getURLSuffix();
        var context = Context.builder().title("文章列表").type("post").layout("index").url(uri).build();
        var paginationMap = PaginationUtil.loadPagination(context.getTemplatePath());
        var url = URL.builder().uri(uri).context(context).fullFilePath(Bored.env().getOutputPath() + "/posts.html").contentType(TEXT_HTML).build()
                .add("pages", Bored.env().getPages())
                .add("pagination", paginationMap.get(0));
        Bored.env().getUrls().put(uri, url);
        log.info("Mapping archive {}", uri);
        paginationMap.forEach(pagination -> {
            var ctx = Context.builder().title("文章列表").type("post").layout("list").url(pagination.getUri()).build();
            var page = URL.builder().uri(pagination.getUri()).context(ctx).contentType(TEXT_HTML)
                    .fullFilePath(Bored.env().getOutputPath() + "/posts.html").build()
                    .add("pages", Bored.env().getPages())
                    .add("pagination", pagination);
            Bored.env().getUrls().put(pagination.getUri(), page);
            log.info("Mapping archive {}", pagination.getUri());
        });
        loadIndex(paginationMap.get(0));
    }

}
