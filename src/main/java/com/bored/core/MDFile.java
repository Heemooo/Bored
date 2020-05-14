package com.bored.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.model.Page;
import com.bored.util.Paths;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class MDFile {

    public MDFile(File file) {
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
        var str = StrUtil.split(Paths.toUrl(absolutePath), "/");
        this.type = StrUtil.nullToDefault(str[1], "");
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
        if (StrUtil.isEmpty(page.getSummary()) || page.getSummary().length() > Bored.config().getSummaryLength()) {
            var str = StrUtil.split(this.content.replaceAll("[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+", ""), Bored.config().getSummaryLength());
            page.setSummary(str[0]);
        }
        page.setContent(MDFile.toHTML(this.content));
        if (Objects.isNull(page.getCategories())) {
            page.setCategories(new ArrayList<>());
        }
        if (Objects.isNull(page.getTags())) {
            page.setTags(new ArrayList<>());
        }
        if (Objects.isNull(page.getType())) {
            page.setType(this.getType());
        }
        page.setOutPutPath(String.format("%s/%s/%s", Paths.outputPath(), page.getType(), this.getHtmlFileName()));
        return page;
    }

    /**
     * markdown文档转html内容
     */
    @SneakyThrows
    public static String toHTML(String content) {
        MutableDataSet options = new MutableDataSet();
        //enable table parse!
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }
}

