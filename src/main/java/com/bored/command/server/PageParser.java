package com.bored.command.server;

import cn.hutool.core.io.file.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 页面解析工具
 * @author https://gitee.com/heemooo
 * @since 2020/3/29
 */
public class PageParser {

    public static Page parse(String path) {
        FileReader fileReader = new FileReader(path);
        List<String> lines = fileReader.readLines();
        AtomicInteger count = new AtomicInteger();
        List<String> header = new ArrayList<>();
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            if (line.contains("---") || line.contains("+++")) {
                count.getAndIncrement();
                continue;
            }
            if (count.get() < 2) {
                header.add(line);
            } else {
                content.append(line);
            }
        }
        Page page = new Page();
        page.setContent(content.toString());
        page.setHeader(header);
        return page;
    }
}
