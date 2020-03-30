package com.bored.command.server;

import cn.hutool.core.io.file.FileReader;
import com.moandjiezana.toml.Toml;

public class SiteConfigReader implements Reader<SiteConfig> {
    @Override
    public SiteConfig read(String path) {
        FileReader config = new FileReader(path);
        Toml toml = new Toml().read(config.getInputStream());
        return toml.to(SiteConfig.class);
    }
}
