package com.bored.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.model.Page;
import com.bored.model.Site;
import com.github.houbb.markdown.toc.core.impl.AtxMarkdownToc;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 页面解析工具
 * @author https://gitee.com/heemooo
 * @since 2020/3/29
 */
@Slf4j
public class PageUtil {

    private final Site site;

    private final String path;

    public PageUtil(String path, Site site) {
        this.site = site;
        this.path = path;
    }

    public List<Page> parse() {
        var pages = new ArrayList<Page>();
        var root = PathUtil.convertCorrectPath(this.path + "/content");
        var files = FileUtil.loopFiles(root);
        for (File file : files) {
            var filePath = file.getPath();
            var permLink = StrUtil.removePrefix(filePath, root);
            var fileReader = new FileReader(file);
            var page = parse(fileReader.readLines());
            var toc = AtxMarkdownToc.newInstance().charset("UTF-8").write(false).subTree(false).genTocFile(filePath);
            page.setToc(toc.getTocLines());
            permLink = StrUtil.removeSuffix(permLink, ".md");
            if (site.getEnableHtmlSuffix()) {
                permLink = permLink + ".html";
            }
            permLink = PathUtil.convertCorrectUrl(permLink);
            page.setPermLink(permLink);
            if (Objects.isNull(page.getUrl())) {
                page.setUrl(page.getPermLink());

            }
            if (!page.isDraft()) {
                pages.add(page);
            }
        }
        return pages.stream().sorted(Comparator.comparing(Page::getDate)).collect(Collectors.toList());
    }

    private Page parse(List<String> lines) {
        var count = new AtomicInteger();
        var header = new StringBuilder();
        var content = new StringBuilder();
        for (var line : lines) {
            if (line.contains(site.getFrontMatterSeparator())) {
                count.getAndIncrement();
                continue;
            }
            if (count.get() < 2) {
                header.append(line).append(System.getProperty("line.separator"));
            } else {
                content.append(line).append(System.getProperty("line.separator"));
            }
        }
        //var frontMatter = TomlUtil.tomlToObj(header.toString(), FrontMatter.class);
        //var page = new Page(frontMatter);
        var page = TomlUtil.tomlToObj(header.toString(), Page.class);
        if (Objects.isNull(page.getLayout())) {
            page.setLayout("page");
        }
        if (Objects.isNull(page.getType())) {
            page.setType(StrUtil.EMPTY);
        }
        if (Objects.isNull(page.getDate())) {
            page.setDate(DateUtil.now());
        }
        page.setContent(content.toString());
        //page.setContent(MDTool.markdown2Html(content.toString()));
        return page;
    }
}
