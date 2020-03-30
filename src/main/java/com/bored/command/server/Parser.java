package com.bored.command.server;

public interface Parser<T> {
    T parse(String path);
}
