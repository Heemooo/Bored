package com.bored.core;

public interface Parser<T> {
    T parse(String path);
}
