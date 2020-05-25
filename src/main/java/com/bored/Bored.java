package com.bored;

import cn.hutool.setting.dialect.Props;
import com.bored.core.URL;
import com.bored.core.Variable;
import com.bored.core.command.Command;
import com.bored.core.model.Category;
import com.bored.core.model.Page;
import com.bored.core.model.Site;
import com.bored.core.model.Tag;
import com.bored.core.template.JetTemplateHelper;
import com.bored.util.Pages;
import com.bored.util.Paths;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author heemooo @see https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
public final class Bored {

    /**
     * 默认模板
     */
    public final static Props DEFAULT_TEMPLATE = new Props("default.template.properties");
    /**
     * 常量配置
     */
    public final static Props CONSTANT = new Props("constant.properties");
    /**
     * 根路径
     * System.getProperty("user.dir")
     */
    public final static String ROOT = System.getProperty("user.dir") + "/site-demo1";
    /**
     * 网站配置
     */
    private Site config;
    /**
     * 模板引擎
     */
    private JetTemplateHelper jetTemplateHelper;
    /**
     * 标签列表
     */
    private final List<Tag> TAG_LIST = new ArrayList<>();
    /**
     * 分类列表
     */
    private final List<Category> CATEGORY_LIST = new ArrayList<>();
    /**
     * 文章列表
     * key 即文章的type
     */
    private final Map<String, List<Page>> PAGE_MAPS = new HashMap<>();
    /**
     * url map
     */
    private final Map<String, URL> URL_MAP = new HashMap<>();


    private Bored() {
    }

    private static class BoredHolder {
        private static final Bored INSTANCE = new Bored();
    }

    /**
     * 获取Bored实例
     * @return Bored 实例(单例)
     */
    private static Bored of() {
        return BoredHolder.INSTANCE;
    }

    /**
     * 加载网站配置
     * @param config 配置
     */
    public static void config(Site config) {
        Bored.of().config = config;
        jetTemplateHelper(new JetTemplateHelper(Paths.layoutPath(config.getTheme())));
        globalVariable("site", config);
    }

    /**
     * 获取网站配置
     * @return 网站配置
     */
    public static Site config() {
        return Bored.of().config;
    }

    /**
     * jetTemplateHelper
     * @param helper 模板引擎帮助类
     */
    public static void jetTemplateHelper(JetTemplateHelper helper) {
        Bored.of().jetTemplateHelper = helper;
    }

    /**
     * jetTemplateHelper
     * @return jetTemplateHelper
     */
    public static JetTemplateHelper jetTemplateHelper() {
        return Bored.of().jetTemplateHelper;
    }

    /**
     * 设置全局变量
     * @param name     变量名
     * @param variable 变量实例
     */
    public static void globalVariable(String name, Variable variable) {
        Bored.of().jetTemplateHelper.globalVariable(name, variable);
    }

    /**
     * 添加标签到标签列表
     * @param tag 标签
     */
    public static void tag(Tag tag) {
        Bored.of().TAG_LIST.add(tag);
    }

    /**
     * 获取标签列表
     * @return 标签列表
     */
    public static List<Tag> tags() {
        return Bored.of().TAG_LIST;
    }

    /**
     * 添加分类到分类列表
     * @param category 分类
     */
    public static void category(Category category) {
        Bored.of().CATEGORY_LIST.add(category);
    }

    /**
     * 获取分类列表
     * @return 分类列表
     */
    public static List<Category> categories() {
        return Bored.of().CATEGORY_LIST;
    }

    /**
     * 添加文章到PAGE_MAPS
     * @param page 文章
     */
    public static void page(Page page) {
        if (!Bored.of().PAGE_MAPS.containsKey(page.getType())) {
            Bored.of().PAGE_MAPS.put(page.getType(), new ArrayList<>());
        }
        Bored.of().PAGE_MAPS.get(page.getType()).add(page);
    }

    /**
     * 获取type下的所有文章
     * @param type 即content下的一级目录
     * @return 文章列表
     */
    public static List<Page> pages(String type) {
        return pageRelevance(Pages.sortByDate(Bored.of().PAGE_MAPS.get(type)));
    }

    /**
     * 文章前后关联
     * @param pages 文章列表
     * @return 关联后的文章列表
     */
    private static List<Page> pageRelevance(List<Page> pages) {
        assert pages != null;
        for (int i = 0, len = pages.size(); i < len; i++) {
            if (i < (len - 1)) pages.get(i).setNext(pages.get(i + 1));
            if (i > 0) pages.get(i).setPrev(pages.get(i - 1));
        }
        return pages;
    }

    /**
     * 获取所有文章
     * @return 文章列表
     */
    public static List<Page> pages() {
        List<Page> pages = Bored.of().PAGE_MAPS.values().stream().reduce(new ArrayList<>(), (t1, t2) -> {
            t1.addAll(t2);
            return t1;
        }).stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
        return pageRelevance(pages);
    }

    /**
     * 获取文章合集
     * @return 文章合集
     */
    public static Map<String, List<Page>> pageMaps() {
        return Bored.of().PAGE_MAPS;
    }

    /**
     * 添加url到url容器
     * @param url url
     */
    public static void url(URL url) {
        log.debug("Mapping url {}", url.context().getUrl());
        Bored.of().URL_MAP.put(url.context().getUrl(), url);
    }

    /**
     * 根据uri获取URL实例
     * @param uri uri
     * @return URL实例
     */
    public static Optional<URL> url(String uri) {
        var _404 = Bored.of().URL_MAP.get("/404" + Bored.config().getURLSuffix());
        return Optional.of(Bored.of().URL_MAP.getOrDefault(uri, _404));
    }

    /**
     * 获取url列表
     * @return url列表
     */
    public static List<URL> urls() {
        return new ArrayList<>(Bored.of().URL_MAP.values());
    }

    public static void main(String[] commands) {
        Deque<String> argList = new LinkedList<>(Arrays.asList(commands));
        if (argList.isEmpty()) {
            Command.displayHelp();
            return;
        }
        String command = argList.remove();
        for (Command c : Command.getCommands()) {
            if (c.getName().equals(command)) {
                try {
                    c.execute(argList);
                } catch (IllegalArgumentException iae) {
                    return; // already handled by command
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        log.error("Unknown command " + command + ".");
        Command.displayHelp();
    }

}
