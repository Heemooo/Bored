package com.bored.listen;

import com.bored.Bored;
import com.bored.core.Container;
import com.bored.model.PageFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;

/**
 * page改变监听
 */
@Slf4j
public class PageListener extends FileAlterationListenerAdaptor {
    @Override
    public void onFileChange(File file) {
        log.info("{} is changed", file.getPath());
        var pageFile = new PageFile(file);
        var page = pageFile.toPage();
        var uri = page.getPermLink();
        var url = Container.get(uri);
        url.add("page", page);
        Container.update(url);
        Bored.env().getPages().remove(page);
        log.info("{} is reload", file.getPath());
    }

    @Override
    public void onFileDelete(File file) {
        log.info("{} is deleted", file.getPath());
        var pageFile = new PageFile(file);
        var uri = pageFile.toPage().getPermLink();
        Container.delete(uri);
    }

    @Override
    public void onFileCreate(File file) {
        log.info("{} is created", file.getPath());
        var pageFile = new PageFile(file);
        var page = pageFile.toPage();
        var url = pageFile.pageToURL(page);
        Container.put(url.uri, url);
        Bored.env().getPages().add(page);
        log.info("Mapping page {}", url.uri);
    }
}
