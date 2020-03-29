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

    public Path(String path) {
        this.path = path;
    }

    public Path(String path, String name) {
        this.path = path;
        this.name = name;
        this.initQueue();
        this.create();
    }

    public String name;
    public String path;
    private Queue<String> folders = new LinkedList<>();
    private Queue<DefaultFile> files = new LinkedList<>();

    public abstract void initQueue();

    public Path addFolder(String path) {
        this.folders.add(Bored.replaceSlash(path));
        return this;
    }

    public Path addFiles(DefaultFile.DefaultFileInit defaultFileInit) {
        DefaultFile defaultFile = new DefaultFile();
        defaultFileInit.init(defaultFile);
        this.files.add(defaultFile);
        return this;
    }

    private void create() {
        folders.forEach(folder -> {
            log.info("Create folder: {}", folder);
            FileUtil.mkdir(folder);
        });
        files.forEach(file -> {
            log.info("Create file: {}", file.getFilePath());
            FileUtil.touch(file.getFilePath());
            try {
                @Cleanup FileWriter writer = new FileWriter(file.getFilePath());
                writer.write(file.getContent());
            } catch (IOException e) {
                log.error("",e);
            }
        });
    }
}
