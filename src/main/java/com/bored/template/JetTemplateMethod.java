package com.bored.template;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.bored.core.Page;
import com.bored.model.PageFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JetTemplateMethod {
    /**
     * 取最新的number条
     * @param pageFiles 文章列表
     * @param number    数量
     * @return 最新的文章
     */
    public static List<PageFile> top(List<PageFile> pageFiles, int number) {
        return pageFiles.subList(0, number);
    }

    /**
     * 对页面列表进行分组，目前包含按年分组和按月分组
     * @param pages 页面列表
     * @param type  {year,month}
     * @return 分组集合
     */
    public static TreeMap<String, List<Page>> groupBy(List<Page> pages, String type) {
        var dateFormat = "yyyy";
        if (type.trim().equals("month")) {
            dateFormat = "yyyy-MM";
        }
        String finalDateFormat = dateFormat;

        Map<String, List<Page>> map = pages.stream()
                .collect(Collectors.groupingBy(page -> DateUtil.format(page.getDate(), finalDateFormat)));
        return MapUtil.sort(map, Comparator.reverseOrder());
    }
}
