package com.bored.server.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractContainer<T> implements Container<T> {

    /**
     * urlMapping
     */
    private final Map<String, T> URLMapping = new HashMap<>();

    public AbstractContainer() {
        init();
    }

    @Override
    public void add(String url, T object) {
        this.URLMapping.put(url, object);
    }

    @Override
    public T get(String url) {
        return this.URLMapping.get(url);
    }

    @Override
    public boolean contains(String url) {
        return this.URLMapping.containsKey(url);
    }

    @Override
    public List<T> list() {
        return null;
    }
}
