package com.bored.util;

import cn.hutool.core.date.DateUtil;
import com.bored.model.PageFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoredUtil {

    private BoredUtil() {
    }

    private static class BoredUtilHolder {
        private static final BoredUtil INSTANCE = new BoredUtil();
    }

    public static BoredUtil of() {
        return BoredUtilHolder.INSTANCE;
    }

    private static List<PageFile> pageFiles;

    public void setPages(List<PageFile> pages1) {
        pageFiles = pages1;
    }

    public static List<PageFile> getByFolder(String folderName) {
        return pageFiles.stream().filter(page -> page.getPermLink().startsWith(folderName)).collect(Collectors.toList());
    }

    public static Map<String, List<PageFile>> groupByYear() {
        List<PageFile> pageFileList = pageFiles.stream().sorted(Comparator.comparing(PageFile::getDate).reversed()).collect(Collectors.toList());
        return pageFileList.stream().collect(Collectors.groupingBy(page -> {
            var date = page.getDate();
            return String.valueOf(DateUtil.year(DateUtil.parseDate(date)));
        }));
    }
}
