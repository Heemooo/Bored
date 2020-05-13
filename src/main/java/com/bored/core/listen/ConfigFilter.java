package com.bored.core.listen;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;

public class ConfigFilter implements IOFileFilter {
    @Override
    public boolean accept(File file) {
        return accept(file, file.getName());
    }

    @Override
    public boolean accept(File file, String name) {
        return "config.toml".equals(name);
    }
}
