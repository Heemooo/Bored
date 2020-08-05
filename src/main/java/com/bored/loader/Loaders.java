package com.bored.loader;

import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.MDFile;
import com.bored.util.FileUtil;
import com.bored.util.Pages;
import com.bored.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Loaders {

    private static final List<Loader> LOADER_LIST = List.of(
            ErrorLoader.INSTANCE,
            StaticLoader.INSTANCE,
            TagLoader.INSTANCE,
            CategoryLoader.INSTANCE,
            ArchiveLoader.INSTANCE,
            ListLoader.INSTANCE,
            HomeLoader.INSTANCE
    );

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private Loaders() {

    }

    public static void loading() {
        var paths = FileUtil.loopFiles(Paths.pagePath());
        for (Path path : paths) {
            var mdFile = MDFile.load(path.toFile());
            var page = mdFile.toPage();
            /*不加载根目录下的md文件到list列表中*/
            if (StrUtil.count(page.getPermLink(), "/") == 1) {
                var url = Pages.toContext(page);
                Bored.url(url);
            } else {
                Bored.page(page);
            }
        }
        Bored.pages().forEach(page -> Bored.url(Pages.toContext(page)));
        LOADER_LIST.forEach(loader -> THREAD_POOL.execute(loader::loading));
    }
}
