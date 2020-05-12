package com.bored.listen;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

public class PageFilter implements IOFileFilter {
    @Override
    public boolean accept(File file) {
        return true;
    }

    @Override
    public boolean accept(File file, String name) {
        return StrUtil.endWith(name, ".md");
    }
}
