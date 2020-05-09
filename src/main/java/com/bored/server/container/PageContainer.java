package com.bored.server.container;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.Context;
import com.bored.core.HTML;
import com.bored.core.Page;
import com.bored.model.Environment;
import com.bored.model.PageFile;
import com.bored.util.PathUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PageContainer extends AbstractContainer<HTML> {

    private String pagePath;

    private Environment env;

    /**
     * list
     */
    private final List<Page> data = new ArrayList<>();

    @Override
    public void init() {
        this.env = Bored.of().getEnv();
        this.pagePath = env.getPagePath();
        this.data.addAll(loadPages());
    }


    public List<Page> loadPages() {
        var files = FileUtil.loopFiles(pagePath);
        List<Page> pages = new ArrayList<>();
        for (File file : files) {
            var pageFile = parse(file);
            var page = pageFile.toPage();
            Context context = new Context();
            context.setTime(page.getDate());
            context.setTitle(page.getTitle());
            context.setUrl(page.getPermLink());
            context.setType(pageFile.getFrontMatter().getType());
            context.setLayout(pageFile.getFrontMatter().getLayout());
            HTML html = new HTML();
            var fullFilePath = String.format("%s/%s/%s", env.getOutputPath(), context.getType(), pageFile.getFileName());
            html.setFullFilePath(fullFilePath);
            html.setContext(context);
            html.setUrl(context.getUrl());
            html.setContent(page.getContent());
            html.setContentType("text/html;charset=utf-8");
            this.add(html.getUrl(), html);
            pages.add(page);
            log.info("Mapping page {}", html.getUrl());
        }
        List<Page> sorts = pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
        for (int i = 0, len = sorts.size(); i < len; i++) {
            if (i < (len - 1)) sorts.get(i).setNext(sorts.get(i + 1));
            if (i > 0) sorts.get(i).setPrev(sorts.get(i - 1));
        }
        return sorts;
    }

    public PageFile parse(File file) {
        var site = Bored.of().getEnv().getSiteConfig();
        var pagePath = Bored.of().getEnv().getPagePath();
        var filePath = file.getPath();
        var pageFile = new PageFile(file);
        var permLink = StrUtil.removePrefix(filePath, pagePath);
        permLink = PathUtil.convertCorrectUrl(StrUtil.removeSuffix(permLink, ".md") + site.getURLSuffix());
        pageFile.setPermLink(permLink);
        return pageFile;
    }

}
