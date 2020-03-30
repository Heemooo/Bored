package com.bored.command.server;

import com.bored.core.AbstractConfig;
import lombok.Data;

@Data
public class SiteConfig extends AbstractConfig {

    private String baseUrl;

    private String language;

    private String title;

    @Override
    public void init() {

    }
}
