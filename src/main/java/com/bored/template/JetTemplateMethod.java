package com.bored.template;

import cn.hutool.core.date.DateUtil;
import com.bored.model.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JetTemplateMethod {
    public static Map<String, List<Page>> groupBy(List<Page> pages, String type) {
        return pages.stream().collect(Collectors.groupingBy(page -> {
            var date = page.getDate();
            var year = DateUtil.year(DateUtil.parseDate(date));
            var mounth = DateUtil.month(DateUtil.parseDate(date))+1;
            return year+"-"+mounth;
        }));
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.monthEnum(DateUtil.parseDate("2019-01-02")));
    }
}
