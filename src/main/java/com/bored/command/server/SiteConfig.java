package com.bored.command.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.bored.core.Bored;
import com.moandjiezana.toml.Toml;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class SiteConfig {

    private String title;

    private String baseURL;

    private String theme;

    private Boolean enableHtmlSuffix;

    private String frontMatterSeparator;

    private Map<String, List<Menu>> menu;

    public String getTheme() {
        return Objects.nonNull(theme) ? theme : "default";
    }

    @Data
    private static class Menu {
        private String id;
        private String name;
        private String url;
        private Integer weight;
        private List<Menu> child;
    }

    private Map<String, Object> params;


    public static void main(String[] args) {
        var toml = new Toml();
        var root = Bored.convertCorrectPath(System.getProperty("user.dir") + "/site-demo/config.toml");
        toml.read(FileUtil.file(root));
        var siteConfig = toml.to(SiteConfig.class);
        Console.log(siteConfig.toString());
    }
}
