package com.bored.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.context.StaticContextFactory;
import com.bored.util.Paths;

import java.io.File;

enum StaticLoader implements Loader {

    /**
     * 唯一的实例
     */
    INSTANCE;

    private static String contentType(String fileName, String filePath) {
        assert fileName != null;
        assert filePath != null;
        if (StrUtil.endWith(fileName, ".css")) {
            return "text/css; charset=utf-8";
        }
        if (StrUtil.endWith(fileName, ".js")) {
            return "application/javascript; charset=utf-8";
        }
        String contentType = FileUtil.getMimeType(filePath);
        if (StrUtil.isEmpty(contentType)) {
            return "application/octet-stream";
        }
        return contentType;
    }

    @Override
    public void loading() {
        var themeName = Bored.config().getTheme();
        var files = FileUtil.loopFiles(Paths.staticPath(themeName));
        for (File file : files) {
            var url = Paths.toUrl(StrUtil.removePrefix(file.getPath(), Paths.themePath(Bored.config().getTheme())));
            var bytes = new FileReader(file.getPath()).readBytes();
            var contentType = contentType(file.getName(), file.getPath());
            var context = new StaticContextFactory(url, contentType, bytes).create();
            Bored.url(context);
        }
    }
}