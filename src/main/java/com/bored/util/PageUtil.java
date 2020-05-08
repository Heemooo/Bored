package com.bored.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Site;
import com.github.houbb.markdown.toc.core.impl.AtxMarkdownToc;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 页面解析工具
 * @author https://gitee.com/heemooo
 * @since 2020/3/29
 */
@Slf4j
public class PageUtil {

    private final Site site;

    private final String pagePath;

    public PageUtil() {
        var env = Bored.of().getEnv();
        this.site = env.getSiteConfig();
        this.pagePath = env.getPagePath();
    }

    public List<Page> loadPages() {
        var pages = new ArrayList<Page>();
        var files = FileUtil.loopFiles(pagePath);
        for (File file : files) {
            var page = parse(file);
            if (!page.isDraft()) {
                pages.add(page);
            }
        }
        return pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
    }

    private Page parse(File file) {
        var filePath = file.getPath();
        var permLink = StrUtil.removePrefix(filePath, pagePath);
        var fileReader = new FileReader(file);
        var page = parseContent(fileReader.readLines());
        page.setToc(parseToc(filePath));
        permLink = StrUtil.removeSuffix(permLink, ".md");
        permLink = permLink + site.getURLSuffix();
        permLink = PathUtil.convertCorrectUrl(permLink);
        page.setPermLink(permLink);

        if (Objects.isNull(page.getUrl())) {
            page.setUrl(page.getPermLink());

        }
        return page;
    }

    @SneakyThrows
    private List<String> parseToc(String filePath) {
        var tocGen = AtxMarkdownToc.newInstance().charset("UTF-8").write(false).subTree(false).genTocFile(filePath);
        return tocGen.getTocLines();
    }

    @SneakyThrows
    private Page parseContent(List<String> lines) {
        var site = Bored.of().getEnv().getSiteConfig();
        var count = 0;
        var header = new StringBuilder();
        var content = new StringBuilder();
        for (var line : lines) {
            if (line.contains(site.getFrontMatterSeparator())) {
                count++;
                continue;
            }
            if (count < 2) {
                header.append(line).append(System.getProperty("line.separator"));
            } else {
                content.append(line).append(System.getProperty("line.separator"));
            }
        }
        var page = TomlUtil.tomlToObj(header.toString(), Page.class);
        if (Objects.isNull(page.getDate())) {
            page.setDate(DateUtil.now());
        }
        page.setContent(content.toString());
        return page;
    }
}
