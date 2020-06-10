package com.bored.core.loader;

import com.bored.Bored;
import com.bored.core.URL;
import com.bored.core.constant.DefaultTemplate;
import com.bored.core.model.Context;
import com.bored.core.model.Tag;
import com.bored.util.Paths;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagLoader {
     CategoryLoader tags() {
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