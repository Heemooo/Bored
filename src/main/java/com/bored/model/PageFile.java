package com.bored.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.Context;
import com.bored.core.Page;
import com.bored.core.Paths;
import com.bored.core.URL;
import com.bored.util.MarkdownUtil;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class PageFile {

    public PageFile(File file) {
        this.file = file;
        this.fileName = file.getName();
        this.htmlFileName = StrUtil.removeSuffix(file.getName(), ".md") + ".html";
        var fileReader = new FileReader(file);
        var headerAndContent = parseLine(fileReader.readLines());
        this.frontMatter = FrontMatter.toObject(headerAndContent[0]);
        this.content = headerAndContent[1];
        var permLink = StrUtil.removePrefix(file.getPath(), Paths.pagePath());
        this.permLink = Paths.toUrl(StrUtil.removeSuffix(permLink, ".md") + Bored.config().getURLSuffix());
        //获取一级目录
        var absolutePath = StrUtil.removePrefix(file.getPath(), Paths.pagePath());
        this.type = StrUtil.split(absolutePath, "/")[1];
    }

    private String type;

    private String htmlFileName;

    private String fileName;

    private File file;

    private FrontMatter frontMatter;

    private String permLink;

    private String content;

    private List<String> categories;

    private List<String> tags;

    private String[] parseLine(List<String> lines) {
        var count = 0;
        var header = new StringBuilder();
        var content = new StringBuilder();
        for (var line : lines) {
            if (line.contains(Bored.config().getFrontMatterSeparator())) {
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

    public Page toPage() {
        var page = new Page();
        BeanUtil.copyProperties(this.getFrontMatter(), page);
        if (StrUtil.isNotBlank(this.getFrontMatter().getUrl())) {
            page.setPermLink(this.getFrontMatter().getUrl());
        } else {
            page.setPermLink(this.getPermLink());
        }
        if (Objects.isNull(page.getDate())) {
            page.setDate(DateUtil.date());
        }
        if (StrUtil.isEmpty(page.getDescription()) || page.getDescription().length() > 200) {
            var str = StrUtil.split(this.content.replaceAll("[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+", ""), 200);
            page.setDescription(str[0]);
        }
        page.setContent(MarkdownUtil.markdown2Html(this.content));
        if (Objects.isNull(page.getCategories())) {
            page.setCategories(new ArrayList<>());
        }
        if (Objects.isNull(page.getTags())) {
            page.setTags(new ArrayList<>());
        }
        if (Objects.isNull(page.getType())) {
            page.setType(this.getType());
        }
        return page;
    }

    public URL pageToURL(Page page) {
        var context = Context.builder()
                .time(page.getDate()).title(page.getTitle()).url(page.getPermLink()).type(this.getFrontMatter().getType())
                .layout(this.getFrontMatter().getLayout()).build();
        return URL.builder()
                .fullFilePath(String.format("%s/%s/%s", Paths.outputPath(), context.type, this.getHtmlFileName()))
                .uri(context.url)
                .context(context)
                .contentType("text/html;charset=utf-8")
                .build().add("page", page);
    }

}

