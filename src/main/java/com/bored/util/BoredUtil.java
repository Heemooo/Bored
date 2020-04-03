package com.bored.util;

import cn.hutool.core.date.DateUtil;
import com.bored.model.Page;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoredUtil {

    private BoredUtil() {
    }

    private static class BoredUtilHolder {
        private static BoredUtil INSTANCE = new BoredUtil();
    }

    public static BoredUtil of() {
        return BoredUtilHolder.INSTANCE;
    }

    private static List<Page> pages;

    public void setPages(List<Page> pages1) {
        pages = pages1;
    }

    public static List<Page> getByFolder(String folderName) {
        return pages.stream().filter(page -> page.getPermLink().startsWith(folderName)).collect(Collectors.toList());
    }

    public static Map<Integer, List<Page>> groupByYear() {
        List<Page> pageList = pages.stream().sorted(Comparator.comparing(Page::getDate)).collect(Collectors.toList());
        return pageList.stream().collect(Collectors.groupingBy(page -> {
            var date = page.getDate();
            return DateUtil.year(DateUtil.parseDate(date));
        }));
    }

    public static String test(){
        return "<h1>nihao</h1>";
    }
}