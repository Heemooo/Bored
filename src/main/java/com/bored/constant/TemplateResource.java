package com.bored.constant;

import cn.hutool.core.io.file.FileReader;

/**
 * 静态模板资源
 * @author https://gitee.com/heemooo
 * @since 2020/3/29
 */
public class TemplateResource {
    /**
     * templates/archetypes/default.md
     */
    public final static String ARCHETYPES_DEFAULT_MD = new FileReader("templates/archetypes/default.md").readString();
    /**
     * templates/site/config.toml
     */
    public final static String SITE_CONFIG_TOML = new FileReader("templates/site/config.toml").readString();
    /**
     * templates/themes/LICENSE
     */
    public final static String THEMES_LICENSE = new FileReader("templates/themes/LICENSE").readString();
    /**
     * templates/themes/theme.toml
     */
    public final static String THEMES_CONFIG_TOML = new FileReader("templates/themes/theme.toml").readString();

}
