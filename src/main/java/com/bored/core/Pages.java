package com.bored.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 这个类包含文章列表操作的静态方法{@code static}
 */
public final class Pages {

    private final static List<Page> pages = new ArrayList<>();

    /**
     * 往列表添加文章
     * @param page 文章
     */
    public static void add(Page page) {
        pages.add(page);
    }

    /**
     * 文章按时间排序
     * @return 文章列表
     */
    public static List<Page> sortByDate() {
        return pages.stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
    }

    /**
     * 获取最新的文章列表
     * @param number 前{number}个
     * @return 文章列表
     */
    public static List<Page> top(int number) {
        return sortByDate().subList(0, number);
    }

    /**
     * 对文章列表进行分组，目前包含按年分组和按月分组
     * @param type {year,month}
     * @return 分组集合
     */
    public static TreeMap<String, List<Page>> groupByDate(String type) {
        var dateFormat = "yyyy";
        if (type.trim().equals("month")) {
            dateFormat = "yyyy-MM";
        }
        String finalDateFormat = dateFormat;

        Map<String, List<Page>> map = pages.stream()
                .collect(Collectors.groupingBy(page -> DateUtil.format(page.getDate(), finalDateFormat)));
        return MapUtil.sort(map, Comparator.reverseOrder());
    }

    /**
     * 获取type下的所有文章
     * @param type 即content下的一级目录
     * @return 文章列表
     */
    public static List<Page> listByType(String type) {
        return pages.stream().filter(page -> page.getType().equals(type)).sorted(Comparator.comparing(Page::getDate)).collect(Collectors.toList());
    }

}
