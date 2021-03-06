package com.bored.server.listen;

import cn.hutool.core.lang.Console;
import com.bored.Bored;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;

/**
 * page改变监听
 */
@Slf4j
public class ConfigListener extends FileAlterationListenerAdaptor {
    @Override
    public void onFileChange(File file) {
        Console.log("{} is changed", file.getPath());
        Bored.loadingConfig();
        Console.log("{} is reload", file.getPath());
    }
}
