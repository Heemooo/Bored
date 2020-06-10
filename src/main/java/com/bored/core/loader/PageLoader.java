package com.bored.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.MDFile;
import com.bored.util.Pages;
import com.bored.util.Paths;

import java.io.File;

public class PageLoader {
    TagLoader pages() {
        var files = FileUtil.loopFiles(Paths.pagePath());
        for (File file : files) {
            var mdFile = MDFile.load(file);
            var page = mdFile.toPage();
            /*不加载根目录下的md文件到list列表中*/
            if (StrUtil.count(page.getPermLink(), "/") == 1) {
                var url = Pages.toURL(page);
                Bored.url(url);
            } else {
                Bored.page(page);
            }
        }
        Bored.pages().forEach(page -> Bored.url(Pages.toURL(page)));
        return new TagLoader();
    }
}