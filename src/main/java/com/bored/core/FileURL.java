package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;

import java.io.BufferedInputStream;

public class FileURL extends URL {

    @Override
    public BufferedInputStream content() {
        return new FileReader(this.getContent()).getInputStream();
    }

    @Override
    public void out() {
        FileUtil.writeBytes(new FileReader(this.getContent()).readBytes(), this.getFullFilePath());
    }
}
