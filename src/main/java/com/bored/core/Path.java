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
    private Queue<String> folders = new LinkedList<>();
    private Queue<DefaultFile> files = new LinkedList<>();

    public abstract Path initQueue(String path);

    public Path addFolder(String path){
        this.folders.add(Bored.replaceSlash(path));
        return this;
    }

    public Path addFiles(DefaultFile.DefaultFileInit defaultFileInit){
        DefaultFile defaultFile = new DefaultFile();
        DefaultFile.DefaultFileContent content = new DefaultFile.DefaultFileContent();
        defaultFile.setContent(content);
        defaultFileInit.init(defaultFile);
        this.files.add(defaultFile);
        return this;
    }

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
