package com.bored.listen;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

public class PageFilter implements IOFileFilter {
    @Override
    public boolean accept(File file) {
        return true;
    }

    @Override
    public boolean accept(File file, String name) {
        return true;
    }
}
