package com.bored.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Site;
import com.github.houbb.markdown.toc.core.impl.AtxMarkdownToc;
import com.youbenzi.mdtool.tool.MDTool;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 页面解析工具
 * @author https://gitee.com/heemooo
 * @since 2020/3/29
 */
@Slf4j
public class PageUtil {

    private Site site;

    private String path;

    public PageUtil(String path, Site site) {
        this.site = site;
        this.path = path;
    }

    public List<Page> parse() {
        var pages = new ArrayList<Page>();
        var root = Bored.convertCorrectPath(this.path + "/content");
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
            permLink = Bored.convertCorrectUrl(permLink);
            page.setPermLink(permLink);
            pages.add(page);
        }
        return pages;
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
        var frontMatter = TomlUtil.tomlToObj(header.toString(), Page.FrontMatter.class);
        var page = new Page(frontMatter);
        page.setContent(MDTool.markdown2Html(content.toString()));
        return page;
    }
}
