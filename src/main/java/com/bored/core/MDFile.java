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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public final class MDFile {

    /**
     * 文件实例
     */
    private File file;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章页输出路径
     */
    private String outPutPath;

    /**
     * 文章前辅文
     */
    private FrontMatter frontMatter;

    public MDFile(File file, String content, String outPutPath, FrontMatter frontMatter) {
        this.file = file;
        this.content = content;
        this.outPutPath = outPutPath;
        this.frontMatter = frontMatter;
    }

    /**
     * 加载文件
     * @param file 文件
     * @return md对象
     */
    public static MDFile load(File file) {
        var fileReader = new FileReader(file);
        var headerAndContent = parseLine(fileReader.readLines());
        var outPutPath = Paths.outputPath() + StrUtil.removePrefix(file.getPath(), Paths.pagePath());
        return new MDFile(file, headerAndContent[1], outPutPath, FrontMatter.toObject(headerAndContent[0]));
    }

    /**
     * 转换成page对象
     * @return page对象
     */
    public Page toPage() {
        var page = new Page();
        BeanUtil.copyProperties(this.getFrontMatter(), page);
        page.setContent(MDFile.toHTML(this.getContent()));
        page.setOutPutPath(this.getOutPutPath());
        page.setType(StrUtil.nullToDefault(this.getFrontMatter().getType(), parseType(this.getFile().getName())));
        page.setPermLink(StrUtil.nullToDefault(this.getFrontMatter().getUrl(), permLink(this.getFile().getPath())));
        if (Objects.isNull(page.getDate())) {
            page.setDate(DateUtil.date());
        }
        if (StrUtil.isEmpty(page.getSummary()) && this.getContent().length() > 0) {
            var summaryLength = Math.min(this.getContent().length(), Bored.config().getSummaryLength());
            var str = StrUtil.split(this.getContent().replaceAll(Bored.CONSTANT.getStr("summaryReg"), StrUtil.EMPTY), summaryLength);
            page.setSummary(str[0]);
        }
        return page;
    }

    /**
     * 类型此值将自动派生自目录
     */
    public static String parseType(String filePath) {
        var absolutePath = StrUtil.removePrefix(filePath, Paths.pagePath());
        var str = StrUtil.split(Paths.toUrl(absolutePath), "/");
        return StrUtil.nullToDefault(str[1], "");
    }

    /**
     * 利用文件路径生成访问浏览器的url
     * @param filePath 文件路径
     * @return url
     */
    public static String permLink(String filePath) {
        var permLink = StrUtil.removePrefix(filePath, Paths.pagePath());
        permLink = Paths.toUrl(StrUtil.removeSuffix(permLink, ".md") + Bored.config().getURLSuffix());
        return permLink;
    }

    /**
     * 解析文章内容行
     * @param lines 内容行
     * @return 文章内容头+内容体
     */
    private static String[] parseLine(List<String> lines) {
        var count = 0;
        var header = new StringBuilder();
        var content = new StringBuilder();
        for (var line : lines) {
            if (line.contains("[^_^]:<>(---)")) {
                count++;
                continue;
            }
            if (count < 2) {
                String str = StrUtil.removePrefix(line, "[^_^]:<>(");
                header.append(StrUtil.removeSuffix(str, ")")).append(System.getProperty("line.separator"));
            } else {
                content.append(line).append(System.getProperty("line.separator"));
            }
        }
        String[] headerAndContent = new String[2];
        headerAndContent[0] = header.toString();
        headerAndContent[1] = content.toString();
        return headerAndContent;
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

