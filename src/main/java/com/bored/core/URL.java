package com.bored.core;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public abstract class URL {
    @Setter
    @Getter
    private String url;

    @Setter
    @Getter
    private String fullFilePath;

    @Setter
    @Getter
    private String content;

    @Getter
    private Context context;

    @Setter
    @Getter
    private String contentType = "text/html;charset=utf-8";

    @Getter
    private Map<String, Object> ctx = new HashMap<>();

    public void setContext(Context context) {
        this.context = context;
        ctx.put("this", context);
    }

    public URL add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    public abstract Object content();

    public abstract void out();
}
