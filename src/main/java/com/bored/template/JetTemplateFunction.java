package com.bored.template;

import com.bored.Bored;
import com.bored.model.Label;

import java.util.concurrent.atomic.AtomicReference;

public class JetTemplateFunction {

    public static String tagUrl(String tagName){
        AtomicReference<String> url = new AtomicReference<>();
        Bored.env().getTags().parallelStream().forEach(tag->{
            if (tag.getName().equals(tagName)) {
                url.set(tag.getUrl());
            }
        });
        return url.get();
    }

    public static Label tag(String tagName) {
        AtomicReference<Label> target = new AtomicReference<>();
        Bored.env().getTags().parallelStream().forEach(tag->{
            if (tag.getName().equals(tagName)) {
                target.set(tag);
            }
        });
        return target.get();
    }

    public static Label category(String categoryName) {
        return null;
    }
}
