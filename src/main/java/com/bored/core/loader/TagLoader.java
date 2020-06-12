package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.constant.DefaultTemplate;
import com.bored.core.context.DefaultContextFactory;
import com.bored.core.model.Tag;
import com.bored.util.Paths;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

enum TagLoader implements Loader {
    /**
     * 唯一的实例
     */
    INSTANCE;

    @Override
    public void loading() {
        if (Bored.config().getDisableTags().equals(Boolean.FALSE)) {
            List<Tag> tagList = new ArrayList<>();
            Bored.pages().parallelStream().forEach(page -> Optional.of(page.getTags()).ifPresent(strings -> strings.parallelStream().forEach(tagName -> {
                var uri = "/tag/" + tagName;
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
                var url = tag.toContext();
                Bored.url(url);
            });

            var url = "/tags";
            var type = "base";
            var layout = "tags.html";
            var outputPath = String.format(DefaultTemplate.TAGS_OUTPUT_FORMAT, Paths.outputPath());
            var title = "标签列表";
            var date = new Date();
            var tagsContext = new DefaultContextFactory(url, type, layout, outputPath)
                    .create()
                    .addObject("title", title)
                    .addObject("date", date)
                    .addObject("tags", tags);
            Bored.url(tagsContext);
            Bored.tags().addAll(tags);
        }
    }
}