package com.bored.command.server;

import cn.hutool.core.io.file.FileReader;
import com.youbenzi.mdtool.tool.MDTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 页面解析工具
 * @author https://gitee.com/heemooo
 * @since 2020/3/29
 */
public class PageParser {

    public static Page parse(List<String> lines) {
        var count = new AtomicInteger();
        var header = new ArrayList<String>();
        var content = new StringBuilder();
        for (var line : lines) {
            if (line.contains("---") || line.contains("+++")) {
                count.getAndIncrement();
                continue;
            }
            if (count.get() < 2) {
                header.add(line);
            } else {
                content.append(line).append(System.getProperty("line.separator"));
            }
        }
        return Page.builder().header(header).content(MDTool.markdown2Html(content.toString())).build();
    }
}
