package com.bored.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Site;
import com.github.houbb.markdown.toc.core.impl.AtxMarkdownToc;
import com.youbenzi.mdtool.tool.MDTool;
import jetbrick.util.annotation.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
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

    /**
     * 开始分页
     * @param list     页面列表
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return 单页数据
     */
    public static List<Page> startPage(@NotNull List<Page> list, Integer pageNum, Integer pageSize) {
        //记录总数
        int count = list.size();
        //页数
        Integer pageCount = getPageCount(list,pageSize);
        //开始索引
        int fromIndex = 0;
        //结束索引
        int toIndex = 0;
        if (pageNum > pageCount) {
            pageNum = pageCount;
        }
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        return list.subList(fromIndex, toIndex);
    }

    public static Integer getPageCount(List<Page> list, Integer pageSize) {
        //记录总数
        Integer count = list.size();
        //页数
        int pageCount = 0;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        return pageCount;
    }
}
