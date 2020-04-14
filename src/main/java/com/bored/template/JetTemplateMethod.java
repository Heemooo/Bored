package com.bored.template;

import cn.hutool.core.date.DateUtil;
import com.bored.model.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JetTemplateMethod {

    /**
     * 对页面列表进行分组，目前包含按年分组和按月分组
     * @param pages 页面列表
     * @param type {year,month}
     * @return 分组集合
     */
    public static Map<String, List<Page>> groupBy(List<Page> pages, String type) {
        return pages.stream().collect(Collectors.groupingBy(page -> {
            var date = page.getDate();
            var year = DateUtil.year(DateUtil.parseDate(date));
            if ("year".equals(type.trim())) {
                return year + "";
            }
            if ("month".equals(type.trim())) {
                var mounth = DateUtil.month(DateUtil.parseDate(date)) + 1;
                return year + "-" + mounth;
            }
            return year + "";
        }));
    }



    public static void main(String[] args) {
        System.out.println(DateUtil.monthEnum(DateUtil.parseDate("2019-01-02")));
    }
}
