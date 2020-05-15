package com.bored.core.listen;

import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.core.model.Site;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.util.Objects;

/**
 * page改变监听
 */
@Slf4j
public class ConfigListener extends FileAlterationListenerAdaptor {
    @Override
    public void onFileChange(File file) {
        Console.log("{} is changed", file.getPath());
        Site site = Site.instance();
        Bored.config(site);
        Console.log("{} is reload", file.getPath());
    }
}
