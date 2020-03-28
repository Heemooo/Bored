package com.bored.core;

import cn.hutool.core.io.FileUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public abstract class Path {
    Queue<String> folders = new LinkedList<>();
    Queue<DefaultFile> files = new LinkedList<>();

    public abstract Path initQueue(String path);

    public Path createFolder() {
        folders.forEach(folder -> {
            log.info("Create folder: {}", folder);
            FileUtil.mkdir(folder);
        });
        return this;
    }

    public void createFile() {
        files.forEach(file -> {
            log.info("Create file: {}", file.getFilePath());
            FileUtil.touch(file.getFilePath());
            try {
                @Cleanup FileWriter writer = new FileWriter(file.getFilePath());
                writer.write(file.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
