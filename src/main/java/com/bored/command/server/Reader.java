package com.bored.command.server;

@FunctionalInterface
public interface Reader<T> {
    T read(String path);
}