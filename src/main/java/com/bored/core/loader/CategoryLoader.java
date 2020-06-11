package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.ContentType;
import com.bored.core.constant.DefaultTemplate;
import com.bored.core.model.Category;
import com.bored.core.model.Context;
import com.bored.util.Paths;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

enum CategoryLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    @Override
    public void loading() {
        if (Bored.config().getDisableCategories().equals(Boolean.FALSE)) {
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
                var url = tag.toContext();
                Bored.url(url);
            });
            var uri = "/categories" + Bored.config().getURLSuffix();
            var outPutPath = String.format(DefaultTemplate.CATEGORIES_OUTPUT_FORMAT, Paths.outputPath());
            var categoriesContext = Context.builder()
                    .title("分类列表")
                    .url(uri)
                    .date(new Date())
                    .type("base")
                    .layout("layout")
                    .contentType(ContentType.TEXT_HTML)
                    .outPutPath(outPutPath)
                    .build()
                    .addObject("categories", categories);
            Bored.url(categoriesContext);
            Bored.categories().addAll(categories);
        }
    }
}
