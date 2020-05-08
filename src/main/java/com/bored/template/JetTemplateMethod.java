package com.bored.template;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Pagination;
import com.bored.util.PageUtil;
import com.bored.util.PaginationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JetTemplateMethod {
    /**
     * 取最新的number条
     * @param pages  文章列表
     * @param number 数量
     * @return 最新的文章
     */
    public static List<Page> top(List<Page> pages, int number) {
        return pages.subList(0, number);
    }

    /**
     * 对页面列表进行分组，目前包含按年分组和按月分组
     * @param pages 页面列表
     * @param type  {year,month}
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
                var month = DateUtil.month(DateUtil.parseDate(date)) + 1;
                return year + "-" + month;
            }
            return year + "";
        }));
    }

    public static Pagination start(Pagination pagination) {
        if (pagination.getUri().equals("index"))
        if (Objects.nonNull(pagination.getCurrent())) {

        }
        return null;
    }

}
