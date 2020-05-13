package com.bored.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 这个类包含文章列表操作的静态方法{@code static}
 */
public final class Pages {
    private Pages() {
    }

    /**
     * 文章按时间排序
     * @return 文章列表
     */
    public static List<Page> sortByDate(List<Page> pages) {
        return pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
    }

    /**
     * 获取最新的文章列表
     * @param number 前{number}个
     * @return 文章列表
     */
    public static List<Page> top(List<Page> pages, int number) {
        return pages.subList(0, number);
    }

    /**
     * 对文章列表进行分组，目前包含按年分组和按月分组
     * @param type {year,month}
     * @return 分组集合
     */
    public static TreeMap<String, List<Page>> groupByDate(List<Page> pages, String type) {
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