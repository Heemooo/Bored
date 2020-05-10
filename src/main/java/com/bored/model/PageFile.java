package com.bored.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.Page;
import com.bored.util.TomlUtil;
import com.youbenzi.mdtool.tool.MDTool;
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
        this.htmlFileName = StrUtil.removeSuffix(file.getName(), ".md")+".html";
        var fileReader = new FileReader(file);
        var headerAndContent = parseLine(fileReader.readLines());
        this.frontMatter = TomlUtil.tomlToObj(headerAndContent[0], FrontMatter.class);
        this.content = headerAndContent[1];
    }

    private String htmlFileName;

    private String fileName;

    private File file;

    private FrontMatter frontMatter;

    private String permLink;

    private String content;

    private List<String> categories;

    private List<String> tags;

    private String[] parseLine(List<String> lines) {
        var site = Bored.env().getSiteConfig();
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

    public Page toPage() {
        var page = new Page();
        BeanUtil.copyProperties(this.getFrontMatter(), page);
        if (StrUtil.isNotBlank(this.getFrontMatter().getUrl())) {
            page.setPermLink(this.getFrontMatter().getUrl());
        } else {
            page.setPermLink(this.getPermLink());
        }
        if (Objects.isNull(page.getDate())) {
            page.setDate(DateUtil.now());
        }
        if(StrUtil.isEmpty(page.getDescription())||page.getDescription().length()>200){
            var str = StrUtil.split(this.content.replaceAll("[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+", ""), 200);
            page.setDescription(str[0]);
        }
        page.setContent(MDTool.markdown2Html(this.content));
        if(Objects.isNull(page.getCategories())){
            page.setCategories(new ArrayList<>());
        }
        if(Objects.isNull(page.getTags())){
            page.setTags(new ArrayList<>());
        }
        return page;
    }
}

