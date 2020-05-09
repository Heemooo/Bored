package com.bored.core.parse;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.PageFile;
import com.bored.util.PathUtil;
import com.bored.util.TomlUtil;
import com.github.houbb.markdown.toc.core.impl.AtxMarkdownToc;
import com.youbenzi.mdtool.tool.MDTool;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PageParse {

    public static PageFile parse(File file) {
        var site = Bored.of().getEnv().getSiteConfig();
        var pagePath = Bored.of().getEnv().getPagePath();
        var filePath = file.getPath();
        var fileReader = new FileReader(file);
        var headerAndContent = parseLine(fileReader.readLines());
        var page = TomlUtil.tomlToObj(headerAndContent[0], PageFile.class);
        page.setContent(MDTool.markdown2Html(headerAndContent[1]));
        if (Objects.isNull(page.getDate())) {
            page.setDate(DateUtil.now());
        }
        var tocGen = AtxMarkdownToc.newInstance().charset("UTF-8").write(false).subTree(false).genTocFile(filePath);
        page.setToc(tocGen.getTocLines());
        var permLink = StrUtil.removePrefix(filePath, pagePath);
        permLink = PathUtil.convertCorrectUrl(StrUtil.removeSuffix(permLink, ".md") + site.getURLSuffix());
        page.setPermLink(permLink);
        if (Objects.isNull(page.getUrl())) {
            page.setUrl(page.getPermLink());
        }
        return page;
    }

    private static String[] parseLine(List<String> lines) {
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
        String[] headerAndContent = new String[2];
        headerAndContent[0] = header.toString();
        headerAndContent[1] = content.toString();
        return headerAndContent;
    }
}
