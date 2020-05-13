package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Category;
import com.bored.model.PageFile;
import com.bored.model.Pagination;
import com.bored.model.Tag;
import com.bored.util.PaginationUtil;
import com.bored.util.PathUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Loader {

    private static final String TEXT_HTML = "text/html;charset=utf-8";

    public static void start() {
        new StaticLoader().statics().pages().tags().categories().archive().list()._404();
    }

    private static class StaticLoader {

        private PageLoader statics() {
            var root = PathUtil.convertCorrectPath(Bored.env().getThemePath());
            var path = PathUtil.convertCorrectPath(Bored.env().getStaticPath());
            var files = FileUtil.loopFiles(path);
            for (File file : files) {
                var uri = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), root));
                var fullFilePath = Bored.env().getOutputPath() + uri;
                var url = URL.builder().filePath(file.getPath()).uri(uri).contentType(contentType(file.getName(), file.getPath()))
                        .context(null).fullFilePath(fullFilePath).build();
                URLS.put(uri, url);
                log.info("Mapping static resource {}", uri);
            }
            return new PageLoader();
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
    }

    private static class PageLoader {
        private TagLoader pages() {
            var env = Bored.env();
            var files = FileUtil.loopFiles(env.getPagePath());
            List<Page> pages = new ArrayList<>();
            for (File file : files) {
                var pageFile = new PageFile(file);
                var page = pageFile.toPage();
                var url = pageFile.pageToURL(page);
                URLS.put(url.uri, url);
                /*不加载根目录下的md文件到list列表中*/
                if (StrUtil.count(url.uri, "/") > 1) {
                    pages.add(page);
                }
                log.info("Mapping page {}", url.uri);
            }
            env.setPages(pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList()));
            for (int i = 0, len = env.getPages().size(); i < len; i++) {
                if (i < (len - 1)) env.getPages().get(i).setNext(env.getPages().get(i + 1));
                if (i > 0) env.getPages().get(i).setPrev(env.getPages().get(i - 1));
            }
            return new TagLoader();
        }
    }

    private static class TagLoader {
        private CategoryLoader tags() {
            List<Tag> tagList = new ArrayList<>();
            Bored.env().getPages().parallelStream().forEach(page -> Optional.of(page.getTags()).ifPresent(strings -> strings.parallelStream().forEach(tagName -> {
                var uri = "/tag/" + tagName + Bored.env().getSiteConfig().getURLSuffix();
                var tag = new Tag(tagName, uri);
                tag.getPages().add(page);
                tagList.add(tag);
            })));
            List<Tag> tags = new ArrayList<>();
            tagList.stream().collect(Collectors.groupingBy(Tag::getUrl)).forEach((url, list) -> list.stream().reduce((t1, t2) -> {
                t1.getPages().addAll(t2.getPages());
                return t1;
            }).ifPresent(tags::add));
            tags.parallelStream().forEach(tag -> {
                var url = tag.toURL();
                URLS.put(url.uri, url);
                log.info("Mapping tag {} {}", tag.getName(), url.uri);
            });
            var uri = "/tags" + Bored.env().getSiteConfig().getURLSuffix();
            var context = Context.builder().title("标签列表").type("base").layout("tags").url(uri).build();
            var url = URL.builder().uri(uri)
                    .fullFilePath(Bored.env().getOutputPath() + "/tags.html")
                    .context(context)
                    .contentType(TEXT_HTML).build().add("tags", tags);
            Bored.env().setTags(tags);
            URLS.put(uri, url);
            log.info("Mapping tags {}", uri);
            return new CategoryLoader();
        }
    }

    private static class CategoryLoader {
        private ArchiveLoader categories() {
            List<Category> categoryList = new ArrayList<>();
            Bored.env().getPages().parallelStream().forEach(page -> Optional.of(page.getCategories()).ifPresent(strings -> strings.parallelStream().forEach(categoryName -> {
                var uri = "/tag/" + categoryName + Bored.env().getSiteConfig().getURLSuffix();
                var tag = new Category(categoryName, uri);
                tag.getPages().add(page);
                categoryList.add(tag);
            })));
            List<Category> categories = new ArrayList<>();
            categoryList.stream().collect(Collectors.groupingBy(Category::getUrl)).forEach((url, list) -> list.stream().reduce((t1, t2) -> {
                t1.getPages().addAll(t2.getPages());
                return t1;
            }).ifPresent(categories::add));
            categories.parallelStream().forEach(tag -> {
                var url = tag.toURL();
                URLS.put(url.uri, url);
                log.info("Mapping category {} {}", tag.getName(), url.uri);
            });
            var uri = "/categories" + Bored.env().getSiteConfig().getURLSuffix();
            var context = Context.builder().title("分类列表").type("base").layout("categories").url(uri).build();
            var url = URL.builder().uri(uri)
                    .fullFilePath(Bored.env().getOutputPath() + "/categories.html")
                    .context(context)
                    .contentType(TEXT_HTML).build().add("categories", categories);
            Bored.env().setCategories(categories);
            URLS.put(uri, url);
            log.info("Mapping categories {}", uri);
            return new ArchiveLoader();
        }
    }

    private static class ArchiveLoader {
        private ListLoader archive() {
            var uri = "/archive/posts" + Bored.env().getSiteConfig().getURLSuffix();
            var context = Context.builder().title("归档:Posts").type("post").layout("archive").url(uri).build();
            var url = URL.builder().uri(uri).context(context).contentType(TEXT_HTML).fullFilePath(Bored.env().getOutputPath() + "/archive/posts.html").build()
                    .add("pages", Bored.env().getPages());
            URLS.put(uri, url);
            log.info("Mapping archive {}", uri);
            return new ListLoader();
        }
    }

    private static class ListLoader {
        private _404Loader list() {
            var paginationMap = PaginationUtil.loadPagination("post/list.html");
            paginationMap.forEach(pagination -> {
                var ctx = Context.builder().title("文章列表").type("post").layout("list").url(pagination.getUri()).build();
                var page = URL.builder().uri(pagination.getUri()).context(ctx).contentType(TEXT_HTML)
                        .fullFilePath(Bored.env().getOutputPath() + "/page/" + pagination.getCurrent() + ".html").build()
                        .add("pages", Bored.env().getPages())
                        .add("pagination", pagination);
                URLS.put(pagination.getUri(), page);
                log.info("Mapping archive {}", pagination.getUri());
            });
            loadIndex(paginationMap.get(0));
            return new _404Loader();
        }

        private static void loadIndex(Pagination pagination) {
            var uri = "/index" + Bored.env().getSiteConfig().getURLSuffix();
            var context = Context.builder().title("首页").layout("index").url(uri).build();
            var url = URL.builder().uri(uri).context(context).fullFilePath(Bored.env().getOutputPath() + "/index.html").contentType(TEXT_HTML).build()
                    .add("pages", Bored.env().getPages())
                    .add("pagination", pagination);
            URLS.put(uri, url);
            log.info("Mapping archive {}", uri);
        }
    }

    private static class _404Loader {
        private void _404() {
            var uri = "/404" + Bored.env().getSiteConfig().getURLSuffix();
            var context = Context.builder().title("404").layout("404").url(uri).build();
            var url = URL.builder().uri(uri).context(context).fullFilePath(Bored.env().getOutputPath() + "/404.html").contentType(TEXT_HTML).build();
            URLS.put(uri, url);
            log.info("Mapping 404 {}", uri);
        }
    }
}
