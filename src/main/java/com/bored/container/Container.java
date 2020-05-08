package com.bored.container;

import java.util.List;

public interface Container<T> {

    void init();

    void add(String url, T object);

    T get(String url);

    boolean contains(String url);

    List<T> list();

}
