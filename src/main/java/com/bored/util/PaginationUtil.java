package com.bored.util;

import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.model.Page;
import com.bored.core.model.Pagination;
import jetbrick.util.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {
    public static List<Pagination> loadPagination(List<Page> pages, String type) {
        var paginations = new ArrayList<Pagination>();
        var pageSize = Bored.config().getPageSize();
        int pageCount = getPageCount(pages, pageSize);
        for (int i = 1; i <= pageCount; i++) {
            var pagina = new Pagination();
            pagina.setUri(getPaginationUrl(type, i));
            pagina.setCurrent(i);
            pagina.setPageCount(pageCount);
            pagina.setData(startPage(pages, i, pageSize));
            if (i == 1) {
                pagina.setHasPrev(false);
                pagina.setHasNext(true);
                pagina.setNext(getPaginationUrl(type, i + 1));
            } else if (i == pageCount) {
                pagina.setHasPrev(true);
                pagina.setHasNext(false);
                pagina.setPrev(getPaginationUrl(type, i - 1));
            } else {
                pagina.setHasPrev(true);
                pagina.setHasNext(true);
                pagina.setPrev(getPaginationUrl(type, i - 1));
                pagina.setNext(getPaginationUrl(type, i + 1));
            }
            paginations.add(pagina);
        }
        return paginations;
    }

    private static String getPaginationUrl(String type, int pageSize) {
        if(StrUtil.isBlank(type)) return "/page/" + pageSize + Bored.config().getURLSuffix();
        return "/" + type + "/page/" + pageSize + Bored.config().getURLSuffix();
    }

    /**
     * 开始分页
     * @param list     页面列表
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return 单页数据
     */
    private static List<Page> startPage(@NotNull List<Page> list, Integer pageNum, Integer pageSize) {
        //记录总数
        int count = list.size();
        //页数
        Integer pageCount = getPageCount(list, pageSize);
        //开始索引
        int fromIndex;
        //结束索引
        int toIndex;
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

    private static Integer getPageCount(List<Page> list, Integer pageSize) {
        //记录总数
        Integer count = list.size();
        //页数
        int pageCount;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        return pageCount;
    }
}
