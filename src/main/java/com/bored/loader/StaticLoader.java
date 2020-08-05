package com.bored.loader;

import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.context.StaticContextFactory;
import com.bored.util.FileUtil;
import com.bored.util.Paths;

import java.nio.file.Path;

enum StaticLoader implements Loader {

    /**
     * 唯一的实例
     */
    INSTANCE;

    private static String contentType(Path path) {
        assert path != null;
        var fileName = path.getFileName().toString();
        assert fileName != null;
        if (StrUtil.endWith(fileName, ".css")) {
            return "text/css; charset=utf-8";
        }
        if (StrUtil.endWith(fileName, ".js")) {
            return "application/javascript; charset=utf-8";
        }
        return FileUtil.contentType(path);
    }

    @Override
    public void loading() {
        var themeName = Bored.config().getTheme();
        var paths = FileUtil.loopFiles(Paths.staticPath(themeName));
        for (Path path : paths) {
            var filePath = path.toString();
            var url = Paths.toUrl(StrUtil.removePrefix(filePath, Paths.themePath(Bored.config().getTheme())));
            var bytes = FileUtil.readBytes(path);
            var contentType = contentType(path);
            var context = new StaticContextFactory(url, contentType, bytes).create();
            Bored.url(context);
        }
    }
}