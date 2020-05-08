package com.bored.util;

import com.bored.Bored;
import com.bored.model.Page;
import com.bored.model.Pagination;
import jetbrick.util.annotation.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationUtil {
    public static Map<String, Pagination> loadPagination(String templatePath) {
        var pages = Bored.of().getEnv().getPageContainer().list();
        var paginationMap = new HashMap<String, Pagination>();
        var env = Bored.of().getEnv();
        var pageSize = env.getSiteConfig().getPageSize();
        int pageCount = getPageCount(pages, pageSize);
        for (int i = 1; i <= pageCount; i++) {
            var pagina = new Pagination();
            pagina.setCurrent(i);
            pagina.setPageCount(pageCount);
            pagina.setData(startPage(pages, i, pageSize));
            if (i == 1) {
                pagina.setHasPrev(false);
                pagina.setHasNext(true);
                pagina.setNext(getPaginationUrl(i + 1));
            } else if (i == pageCount) {
                pagina.setHasPrev(true);
                pagina.setHasNext(false);
                pagina.setPrev(getPaginationUrl(i - 1));
            } else {
                pagina.setHasPrev(true);
                pagina.setHasNext(true);
                pagina.setPrev(getPaginationUrl(i - 1));
                pagina.setNext(getPaginationUrl(i + 1));
            }
            var url = getPaginationUrl(i);
            pagina.setTemplatePath(templatePath);
            paginationMap.put(url, pagina);
        }
        return paginationMap;
    }

    private static String getPaginationUrl(int pageSize) {
        return "/pages/" + pageSize + "." + Bored.of().getEnv().getSiteConfig().getURLSuffix();
    }

    /**
     * 开始分页
     * @param list     页面列表
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return 单页数据
     */
    public static List<Page> startPage(@NotNull List<Page> list, Integer pageNum, Integer pageSize) {
        //记录总数
        int count = list.size();
        //页数
        Integer pageCount = getPageCount(list, pageSize);
        //开始索引
        int fromIndex = 0;
        //结束索引
        int toIndex = 0;
        if (pageNum > pageCount) {
            pageNum = pageCount;
        }
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        return list.subList(fromIndex, toIndex);
    }

    public static Integer getPageCount(List<Page> list, Integer pageSize) {
        //记录总数
        Integer count = list.size();
        //页数
        int pageCount = 0;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        return pageCount;
    }
}
