package com.bored;

import com.bored.core.Bored;
import lombok.SneakyThrows;


public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        Bored.run(args, System.getProperty("user.dir") + "site-demo");
    }
}