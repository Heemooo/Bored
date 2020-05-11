package com.bored.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {
    private static final Map<String, URL> URL_LIST = new HashMap<>();

    public static void put(String uri, URL url) {
        URL_LIST.put(uri, url);
    }

    public static boolean contains(String uri) {
        return URL_LIST.containsKey(uri);
    }

    public static URL get(String uri) {
        return URL_LIST.get(uri);
    }

    public static void update(URL url) {
        if (contains(url.uri)) {
            URL_LIST.put(url.uri, url);
        }
    }

    public static List<URL> list(){
        return new ArrayList<>(URL_LIST.values());
    }

}
