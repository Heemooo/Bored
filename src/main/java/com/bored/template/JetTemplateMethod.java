package com.bored.template;

import cn.hutool.core.date.DateUtil;
import com.bored.model.PageFile;

import java.util.*;
import java.util.stream.Collectors;

public class JetTemplateMethod {
    /**
     * 取最新的number条
     * @param pageFiles  文章列表
     * @param number 数量
     * @return 最新的文章
     */
    public static List<PageFile> top(List<PageFile> pageFiles, int number) {
        return pageFiles.subList(0, number);
    }

    /**
     * 对页面列表进行分组，目前包含按年分组和按月分组
     * @param pageFiles 页面列表
     * @param type  {year,month}
     * @return 分组集合
     */
    public static List<Map.Entry<String, List<PageFile>>> groupBy(List<PageFile> pageFiles, String type) {
        Map<String, List<PageFile>> map = pageFiles.stream().collect(Collectors.groupingBy(page -> {
            var date = page.getDate();
            var year = DateUtil.year(DateUtil.parseDate(date));
            if ("year".equals(type.trim())) {
                return year + "";
            }
            if ("month".equals(type.trim())) {
                var month = DateUtil.month(DateUtil.parseDate(date)) + 1;
                return year + "-" + month;
            }
            return year + "";
        }));
        List<Map.Entry<String, List<PageFile>>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        Collections.reverse(list);
        return list;
    }

    /**
     * 根据map的key排序
     * @param map    待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     * @author zero 2019/04/08
     */
    private static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = new HashMap<>();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("2015-06-10", 3);
        map.put("2015-06-09", 2);
        map.put("2015-06-08", 1);
        map.put("2015-06-11", 4);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByKey());
        System.out.println(list.toString());
    }

    /**
     * 根据map的value排序
     * @param map    待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     * @author zero 2019/04/08
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = new HashMap<>();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed())
                    .forEach(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

}
