package com.bored.listen;

import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.core.Site;
import com.bored.util.TomlUtil;
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
        log.info("{} is changed", file.getPath());
        Site site = null;
        try {
            site = TomlUtil.loadTomlFile(file.getPath(), Site.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (Objects.nonNull(site)) {
            Bored.env().setSiteConfig(site);
        }
        log.info("{} is reload", file.getPath());
    }
}
