package com.bored.server.container;

import cn.hutool.core.collection.CollUtil;
import com.bored.Bored;
import com.bored.model.Tag;

public class TagContainer extends AbstractContainer<Tag> {

    @Override
    public void init() {
        var pages = Bored.of().getEnv().getPageContainer().list();
        pages.forEach(page -> {
            var tags = page.getTags();
            if (CollUtil.isNotEmpty(tags)) {
                tags.forEach(tagName -> {
                    var url = String.format("/tag/%s%s", tagName, Bored.of().getEnv().getSiteConfig().getURLSuffix());
                    if (contains(url)) {
                        get(url).getPageFiles().add(page);
                    } else {
                        Tag tag = new Tag(tagName, url);
                        tag.getPageFiles().add(page);
                        add(url, tag);
                        list().add(tag);
                    }
                });
            }
        });
    }
}
