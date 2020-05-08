package com.bored.container;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Category;
import com.bored.model.Page;
import com.bored.model.Site;
import com.bored.model.Tag;
import com.bored.parse.PageParse;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PageContainer extends AbstractContainer<Page> {

    private String pagePath;

    @Override
    public void init() {
        var env = Bored.of().getEnv();
        this.pagePath = env.getPagePath();
        this.list().addAll(loadPages());
    }


    public List<Page> loadPages() {
        var env = Bored.of().getEnv();
        var files = FileUtil.loopFiles(pagePath);
        List<Page> pages = new ArrayList<>();
        for (File file : files) {
            var page = PageParse.parse(file);
            if (!page.isDraft()) {
                pages.add(page);
            }
            if (StrUtil.isNotBlank(page.getPermLink())) {
                this.add(page.getPermLink(), page);
            }
            if (StrUtil.isNotBlank(page.getUrl())) {
                this.add(page.getUrl(), page);
            }
            page.getTags().forEach(tagName -> {
                if (env.getTags().containsKey(tagName)) {
                    env.getTags().get(tagName).getPages().add(page);
                } else {
                    Tag t = new Tag();
                    t.setName(tagName);
                    t.getPages().add(page);
                    t.setUrl(String.format("/tag/%s%s", tagName, Bored.of().getEnv().getSiteConfig().getURLSuffix()));
                    env.getTags().put(tagName, t);
                }
            });
            page.getCategories().forEach(categoryName -> {
                if (env.getCategories().containsKey(categoryName)) {
                    env.getCategories().get(categoryName).getPages().add(page);
                } else {
                    Category category = new Category();
                    category.setName(categoryName);
                    category.getPages().add(page);
                    category.setUrl(String.format("/category/%s%s", categoryName, Bored.of().getEnv().getSiteConfig().getURLSuffix()));
                    env.getCategories().put(categoryName, category);
                }
            });
        }
        List<Page> sorts = pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
        for (int i = 0, len = sorts.size(); i < len; i++) {
            if (i < (len - 1)) sorts.get(i).setNext(sorts.get(i + 1));
            if (i > 0) sorts.get(i).setPrev(sorts.get(i - 1));
        }
        return sorts;
    }
}
