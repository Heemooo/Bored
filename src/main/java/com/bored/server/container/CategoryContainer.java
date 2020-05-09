package com.bored.server.container;

import cn.hutool.core.collection.CollUtil;
import com.bored.Bored;
import com.bored.model.Category;

public class CategoryContainer extends AbstractContainer<Category> {

    @Override
    public void init() {
        var pages = Bored.of().getEnv().getPageContainer().list();
        pages.forEach(page -> {
            var categories = page.getCategories();
            if (CollUtil.isNotEmpty(categories)) {
                categories.forEach(categoryName -> {
                    var url = String.format("/category/%s%s", categoryName, Bored.of().getEnv().getSiteConfig().getURLSuffix());
                    if (contains(url)) {
                        get(url).getPages().add(page);
                    } else {
                        Category category = new Category(categoryName, url);
                        category.getPages().add(page);
                        add(url, category);
                        list().add(category);
                    }
                });

            }
        });
    }
}
