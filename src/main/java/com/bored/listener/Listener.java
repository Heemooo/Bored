package com.bored.listener;


import java.io.File;

/**
 * 监听文件修改
 */
public interface Listener {
    void listen(File file);
}
