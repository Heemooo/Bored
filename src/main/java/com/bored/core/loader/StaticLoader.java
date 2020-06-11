package com.bored.core.loader;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.ContentType;
import com.bored.core.URL;
import com.bored.core.model.Context;
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
            return ContentType.CSS;
        }
        if (StrUtil.endWith(fileName, ".js")) {
            return ContentType.JS;
        }
        String contentType = FileUtil.getMimeType(filePath);
        if (StrUtil.isEmpty(contentType)) {
            return ContentType.OTHER;
        }
        return contentType;
    }

    @Override
    public void loading() {
        var themeName = Bored.config().getTheme();
        var files = FileUtil.loopFiles(Paths.staticPath(themeName));
        for (File file : files) {
            var uri = Paths.toUrl(StrUtil.removePrefix(file.getPath(), Paths.themePath(Bored.config().getTheme())));
            var fullFilePath = Paths.outputPath() + uri;
            var bytes = new FileReader(file.getPath()).readBytes();
            var url = URL.createStaticURL(new Context(uri), contentType(file.getName(), file.getPath()), fullFilePath, bytes);
            Bored.url(url);
        }
    }
}