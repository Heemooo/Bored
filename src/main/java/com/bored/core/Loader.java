package com.bored.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Category;
import com.bored.model.Context;
import com.bored.model.Page;
import com.bored.model.Tag;
import com.bored.util.Pages;
import com.bored.util.PaginationUtil;
import com.bored.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Loader {

    private static final String TEXT_HTML = "text/html;charset=utf-8";

    public static void start() {
        new StaticLoader().statics().pages().tags().categories().archive().list().index()._404();
    }

    private static class StaticLoader {

        private PageLoader statics() {
            var themeName = Bored.config().getTheme();
            var files = FileUtil.loopFiles(Paths.staticPath(themeName));
            for (File file : files) {
                var uri = Paths.toUrl(StrUtil.removePrefix(file.getPath(), Paths.themePath(Bored.config().getTheme())));
                var fullFilePath = Paths.outputPath() + uri;
                var url = URL.builder().filePath(file.getPath()).uri(uri).contentType(contentType(file.getName(), file.getPath()))
                        .context(null).outPutPath(fullFilePath).build();
                Bored.url(url);
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
            var files = FileUtil.loopFiles(Paths.pagePath());
            for (File file : files) {
                var mdFile = new MDFile(file);
                var page = mdFile.toPage();
                /*不加载根目录下的md文件到list列表中*/
                if (StrUtil.count(page.getPermLink(), "/") == 1) {
                    var url = Pages.toURL(page);
                    Bored.url(url);
                } else {
                    Bored.page(page);
                }
            }
            Bored.pages().forEach(page -> Bored.url(Pages.toURL(page)));
            return new TagLoader();
        }
    }

    private static class TagLoader {
        private CategoryLoader tags() {
            List<Tag> tagList = new ArrayList<>();
            Bored.pages().parallelStream().forEach(page -> Optional.of(page.getTags()).ifPresent(strings -> strings.parallelStream().forEach(tagName -> {
                var uri = "/tag/" + tagName + Bored.config().getURLSuffix();
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
                Bored.url(url);
                log.info("Mapping tag {} {}", tag.getName(), url.uri());
            });
            var uri = "/tags" + Bored.config().getURLSuffix();
            var context = Context.builder().title("标签列表").type("base").layout("tags").url(uri).build();
            var url = URL.builder().uri(uri)
                    .outPutPath(Paths.outputPath() + "/tags.html")
                    .context(context)
                    .contentType(TEXT_HTML).build().add("tags", tags);
            Bored.tags().addAll(tags);
            Bored.url(url);
            log.info("Mapping tags {}", uri);
            return new CategoryLoader();
        }
    }

    private static class CategoryLoader {
        private ArchiveLoader categories() {
            List<Category> categoryList = new ArrayList<>();
            Bored.pages().parallelStream().forEach(page -> Optional.of(page.getCategories()).ifPresent(strings -> strings.parallelStream().forEach(categoryName -> {
                var uri = "/category/" + categoryName + Bored.config().getURLSuffix();
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
                Bored.url(url);
                log.info("Mapping category {} {}", tag.getName(), url.uri());
            });
            var uri = "/categories" + Bored.config().getURLSuffix();
            var context = Context.builder().title("分类列表").type("base").layout("categories").url(uri).build();
            var url = URL.builder().uri(uri)
                    .outPutPath(Paths.outputPath() + "/categories.html")
                    .context(context)
                    .contentType(TEXT_HTML).build().add("categories", categories);
            Bored.categories().addAll(categories);
            Bored.url(url);
            log.info("Mapping categories {}", uri);
            return new ArchiveLoader();
        }
    }

    private static class ArchiveLoader {
        private ListLoader archive() {
            var uri = "/archive/posts" + Bored.config().getURLSuffix();
            var context = Context.builder().title("归档:Posts").type("post").layout("archive").url(uri).build();
            var url = URL.builder().uri(uri).context(context).contentType(TEXT_HTML).outPutPath(Paths.outputPath() + "/archive/posts.html").build()
                    .add("pages", Bored.pages());
            Bored.url(url);
            log.info("Mapping archive {}", uri);
            return new ListLoader();
        }
    }

    private static class ListLoader {
        private IndexLoader list() {
            Bored.pageMaps().forEach(ListLoader::loadList);
            return new IndexLoader();
        }

        private static void loadList(String type, List<Page> pageList) {
            List<Page> pages = Pages.sortByDate(pageList);
            var paginationMap = PaginationUtil.loadPagination(pages, type);
            paginationMap.forEach(pagination -> {
                var ctx = Context.builder().title("文章列表").type(type).layout("list").url(pagination.getUri()).build();
                var url = URL.builder().uri(pagination.getUri()).context(ctx).contentType(TEXT_HTML)
                        .outPutPath(Paths.outputPath() + "/" + type + "/page/" + pagination.getCurrent() + ".html").build()
                        .add("pages", pages)
                        .add("pagination", pagination);
                Bored.url(url);
                log.info("Mapping {} page {}", type, pagination.getUri());
            });
        }
    }

    private static class IndexLoader {
        public _404Loader index() {
            List<Page> pages = Bored.pages();
            var paginationList = PaginationUtil.loadPagination(pages, null);
            paginationList.forEach(pagination -> {
                var ctx = Context.builder().title("首页-第" + pagination.getCurrent() + "页").layout("list").url(pagination.getUri()).build();
                var url = URL.builder().uri(pagination.getUri()).context(ctx).contentType(TEXT_HTML)
                        .outPutPath(Paths.outputPath() + "/page/" + pagination.getCurrent() + ".html").build()
                        .add("pages", pages)
                        .add("pagination", pagination);
                Bored.url(url);
                log.info("Mapping page {}", pagination.getUri());
            });
            var uri = "/index" + Bored.config().getURLSuffix();
            var context = Context.builder().title("首页").layout("index").url(uri).build();
            var indexUrl = URL.builder().uri(uri).context(context).outPutPath(Paths.outputPath() + "/index.html").contentType(TEXT_HTML).build()
                    .add("pages", pages)
                    .add("pagination", CollUtil.isNotEmpty(paginationList) ? paginationList.get(0) : List.of());
            Bored.url(indexUrl);
            log.info("Mapping index {}", uri);
            return new _404Loader();
        }
    }

    private static class _404Loader {
        private void _404() {
            var uri = "/404" + Bored.config().getURLSuffix();
            var context = Context.builder().title("404").layout("404").url(uri).build();
            var url = URL.builder().uri(uri).context(context).outPutPath(Paths.outputPath() + "/404.html").contentType(TEXT_HTML).build();
            Bored.url(url);
            log.info("Mapping 404 {}", uri);
        }
    }

}
