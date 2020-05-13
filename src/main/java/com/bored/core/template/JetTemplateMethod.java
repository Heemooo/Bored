package com.bored.core.template;

import com.bored.core.MDFile;
import com.bored.core.model.Page;
import com.bored.util.Pages;

import java.util.List;
import java.util.TreeMap;

public class JetTemplateMethod {
    /**
     * 取最新的number条
     * @param MDFiles 文章列表
     * @param number  数量
     * @return 最新的文章
     */
    public static List<MDFile> top(List<MDFile> MDFiles, int number) {
        return MDFiles.subList(0, number);
    }

    /**
     * 对页面列表进行分组，目前包含按年分组和按月分组
     * @param pages 页面列表
     * @param type  {year,month}
     * @return 分组集合
     */
    public static TreeMap<String, List<Page>> groupBy(List<Page> pages, String type) {
        return Pages.groupByDate(pages, type);
    }
}
