package com.bored.server.container;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Environment;
import com.bored.model.PageFile;
import com.bored.core.parse.PageParse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PageContainer extends AbstractContainer<PageFile> {

    private String pagePath;

    @Override
    public void init() {
        Environment env = Bored.of().getEnv();
        this.pagePath = env.getPagePath();
        this.list().addAll(loadPages());
    }

    public List<PageFile> loadPages() {
        var files = FileUtil.loopFiles(pagePath);
        List<PageFile> pageFiles = new ArrayList<>();
        for (File file : files) {
            var page = PageParse.parse(file);
            if (!page.isDraft()) {
                pageFiles.add(page);
            }
            if (StrUtil.isNotBlank(page.getPermLink())) {
                this.add(page.getPermLink(), page);
            }
            log.info("Mapping page {}", page.getPermLink());
            if (!page.getPermLink().equals(page.getUrl())){
                this.add(page.getUrl(), page);
                log.info("Mapping page {}", page.getUrl());
            }
        }
        List<PageFile> sorts = pageFiles.stream().sorted(Comparator.comparing(PageFile::getDate).reversed()).collect(Collectors.toList());
        for (int i = 0, len = sorts.size(); i < len; i++) {
            if (i < (len - 1)) sorts.get(i).setNext(sorts.get(i + 1));
            if (i > 0) sorts.get(i).setPrev(sorts.get(i - 1));
        }
        return sorts;
    }
}
