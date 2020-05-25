package com.bored.core.loader;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.constant.DefaultTemplate;
import com.bored.core.ContentType;
import com.bored.core.MDFile;
import com.bored.core.URL;
import com.bored.core.model.Category;
import com.bored.core.model.Context;
import com.bored.core.model.Page;
import com.bored.core.model.Tag;
import com.bored.util.Pages;
import com.bored.util.PaginationUtil;
import com.bored.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Loader {

    public static void start() {
        new StaticLoader().statics().pages().tags().categories().archive().list().home()._404();
    }

    private static class StaticLoader {

        private PageLoader statics() {
            var themeName = Bored.config().getTheme();
            var files = FileUtil.loopFiles(Paths.staticPath(themeName));
            for (File file : files) {
                var uri = Paths.toUrl(StrUtil.removePrefix(file.getPath(), Paths.themePath(Bored.config().getTheme())));
                var fullFilePath = Paths.outputPath() + uri;
                var bytes = new FileReader(file.getPath()).readBytes();
                var url = URL.createStaticURL(new Context(uri), contentType(file.getName(), file.getPath()), fullFilePath, bytes);
                Bored.url(url);
            }
            return new PageLoader();
        }

        private static String contentType(String fileName, String filePath) {
            assert fileName != null;
            assert filePath != null;
            if (StrUtil.endWith(fileName, ".css")) {
                return ContentType.CSS;
            }
            if (StrUtil.endWith(fileName, ".js")) {
                return ContentType.JS;
            }
            String contentType = FileUtil.getMimeType(filePath);
            if (StrUtil.isEmpty(contentType)) {
                return ContentType.OTHER;
            }
            return contentType;
        }
    }

    private static class PageLoader {
        private TagLoader pages() {
            var files = FileUtil.loopFiles(Paths.pagePath());
            for (File file : files) {
                var mdFile = MDFile.load(file);
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
            });
            var uri = "/tags" + Bored.config().getURLSuffix();
            var context = new Context("标签列表", uri, new Date(), DefaultTemplate.TAGS_TEMPLATE);
            var outPutPath = String.format(DefaultTemplate.TAGS_OUTPUT_FORMAT, Paths.outputPath());
            var url = URL.createHTMLURL(context, outPutPath).add("tags", tags);
            Bored.url(url);
            Bored.tags().addAll(tags);
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
            });
            var uri = "/categories" + Bored.config().getURLSuffix();
            var context = new Context("分类列表", uri, new Date(), DefaultTemplate.CATEGORIES_TEMPLATE);
            var outPutPath = String.format(DefaultTemplate.CATEGORIES_OUTPUT_FORMAT, Paths.outputPath());
            var url = URL.createHTMLURL(context, outPutPath).add("categories", categories);
            Bored.url(url);
            Bored.categories().addAll(categories);
            return new ArchiveLoader();
        }
    }

    private static class ArchiveLoader {
        private ListLoader archive() {
            var uri = "/archives" + Bored.config().getURLSuffix();
            var context = new Context("归档:Posts", uri, new Date(), DefaultTemplate.ARCHIVE_TEMPLATE);
            var outPutPath = Paths.outputPath() + "/archive.html";
            var url = URL.createHTMLURL(context, outPutPath).add("pages", Bored.pages());
            Bored.url(url);
            return new ListLoader();
        }
    }

    private static class ListLoader {
        private HomeLoader list() {
            Bored.pageMaps().forEach(ListLoader::loadList);
            return new HomeLoader();
        }

        private static void loadList(String type, List<Page> pageList) {
            List<Page> pages = Pages.sortByDate(pageList);
            var paginationMap = PaginationUtil.loadPagination(pages, type);
            paginationMap.forEach(pagination -> {
                var context = new Context("文章列表", pagination.getUri(), type, "list", new Date());
                var outPutPath = Paths.outputPath() + "/" + type + "/page/" + pagination.getCurrent() + ".html";
                var url = URL.createHTMLURL(context, outPutPath)
                        .add("pages", pages)
                        .add("pagination", pagination);
                Bored.url(url);
            });
        }
    }

    private static class HomeLoader {
        public _404Loader home() {
            List<Page> pages = Bored.pages();
            var paginationList = PaginationUtil.loadPagination(pages, null);
            paginationList.forEach(pagination -> {
                var context = new Context("首页-第" + pagination.getCurrent() + "页", pagination.getUri(), new Date(), DefaultTemplate.HOME_TEMPLATE);
                var outPutPath = Paths.outputPath() + "/page/" + pagination.getCurrent() + ".html";
                var url = URL.createHTMLURL(context, outPutPath)
                        .add("pages", pages)
                        .add("pagination", pagination);
                Bored.url(url);
            });
            var uri = "/index" + Bored.config().getURLSuffix();
            var context = new Context("首页", uri, new Date(), DefaultTemplate.HOME_TEMPLATE);
            var outPutPath = Paths.outputPath() + "/index.html";
            var indexUrl = URL.createHTMLURL(context, outPutPath)
                    .add("pages", pages)
                    .add("pagination", CollUtil.isNotEmpty(paginationList) ? paginationList.get(0) : List.of());
            Bored.url(indexUrl);
            return new _404Loader();
        }
    }

    private static class _404Loader {

        private void _404() {
            var uri = "/404" + Bored.config().getURLSuffix();
            var outPutPath = Paths.outputPath() + "/404.html";
            var context = new Context("404 Not Found", uri, new Date(), DefaultTemplate._404_TEMPLATE);
            if (Bored.jetTemplateHelper().checkTemplate(context.template())) {
                var url = URL.createHTMLURL(context, outPutPath);
                Bored.url(url);
            } else {
                var bytes = Bored.CONSTANT.getStr("404.default.content").getBytes(StandardCharsets.UTF_8);
                var url = URL.createDefaultURL(context, outPutPath, bytes);
                Bored.url(url);
            }
        }
    }

}
