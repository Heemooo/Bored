package com.bored.command.server.handler;

import com.bored.command.server.SiteConfig;

public interface Mapping {
    void init(String rootPath, SiteConfig config);
}
