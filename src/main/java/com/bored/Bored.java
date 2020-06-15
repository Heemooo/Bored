package com.bored;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.command.Command;
import com.bored.context.Context;
import com.bored.loader.Loaders;
import com.bored.model.Variable;
import com.bored.model.bean.Category;
import com.bored.model.bean.Page;
import com.bored.model.bean.Site;
import com.bored.model.bean.Tag;
import com.bored.server.listen.ConfigFilter;
import com.bored.server.listen.ConfigListener;
import com.bored.template.JetTemplateHelper;
import com.bored.util.Pages;
import com.bored.util.Paths;
import com.moandjiezana.toml.Toml;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author heemooo @see https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
public enum Bored {

    /**
     * 唯一实例
     */
    INSTANCE;

    public static final Map<String, Object> SHORT_TPL = new HashMap<>();
    /**
     * 根路径
     * System.getProperty("user.dir")
     */
    public final static String ROOT = System.getProperty("user.dir") + "/site-demo1";
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
    private final Map<String, Context> URL_MAP = new HashMap<>();
    /**
     * 网站配置
     */
    private Site config;
    /**
     * 模板引擎
     */
    private JetTemplateHelper jetTemplateHelper;

    public static Object shortTpl(String key) {
        if (SHORT_TPL.isEmpty()) {
            var toml = new Toml();
            var file = FileUtil.file("short.template.toml");
            toml.read(file);
            SHORT_TPL.putAll(toml.toMap());
        }
        return SHORT_TPL.getOrDefault(key, StrUtil.EMPTY);
    }

    /**
     * 加载配置
     */
    public static void loadingConfig() {
        var config = Site.instance();
        Bored.INSTANCE.config = config;
        jetTemplateHelper(new JetTemplateHelper(Paths.layoutPath(config.getTheme())));
        globalVariable(Site.class, "site", config);
    }

    /**
     * 加载文件
     */
    public static void loadingFiles() {
        Loaders.loading();
    }

    /**
     * 开始监听配置文件
     */
    public static void listingConfig() {
        // 每隔1000毫秒扫描一次
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000L);
        FileFilter filter = FileFilterUtils.and(new ConfigFilter());
        FileAlterationObserver observer = new FileAlterationObserver(Bored.ROOT, filter);
        observer.addListener(new ConfigListener());
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取网站配置
     * @return 网站配置
     */
    public static Site config() {
        return Bored.INSTANCE.config;
    }

    /**
     * jetTemplateHelper
     * @param helper 模板引擎帮助类
     */
    public static void jetTemplateHelper(JetTemplateHelper helper) {
        Bored.INSTANCE.jetTemplateHelper = helper;
    }

    /**
     * jetTemplateHelper
     * @return jetTemplateHelper
     */
    public static JetTemplateHelper jetTemplateHelper() {
        return Bored.INSTANCE.jetTemplateHelper;
    }

    /**
     * 设置全局变量
     * @param clazz    变量类型
     * @param name     变量名
     * @param variable 变量实例
     */
    public static void globalVariable(Class<?> clazz, String name, Variable variable) {
        Bored.INSTANCE.jetTemplateHelper.globalVariable(clazz, name, variable);
    }

    /**
     * 添加标签到标签列表
     * @param tag 标签
     */
    public static void tag(Tag tag) {
        Bored.INSTANCE.TAG_LIST.add(tag);
    }

    /**
     * 获取标签列表
     * @return 标签列表
     */
    public static List<Tag> tags() {
        return Bored.INSTANCE.TAG_LIST;
    }

    /**
     * 添加分类到分类列表
     * @param category 分类
     */
    public static void category(Category category) {
        Bored.INSTANCE.CATEGORY_LIST.add(category);
    }

    /**
     * 获取分类列表
     * @return 分类列表
     */
    public static List<Category> categories() {
        return Bored.INSTANCE.CATEGORY_LIST;
    }

    /**
     * 添加文章到PAGE_MAPS
     * @param page 文章
     */
    public static void page(Page page) {
        if (!Bored.INSTANCE.PAGE_MAPS.containsKey(page.getType())) {
            Bored.INSTANCE.PAGE_MAPS.put(page.getType(), new ArrayList<>());
        }
        Bored.INSTANCE.PAGE_MAPS.get(page.getType()).add(page);
    }

    /**
     * 获取type下的所有文章
     * @param type 即content下的一级目录
     * @return 文章列表
     */
    public static List<Page> pages(String type) {
        return pageRelevance(Pages.sortByDate(Bored.INSTANCE.PAGE_MAPS.get(type)));
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
        List<Page> pages = Bored.INSTANCE.PAGE_MAPS.values().stream().reduce(new ArrayList<>(), (t1, t2) -> {
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
        return Bored.INSTANCE.PAGE_MAPS;
    }

    /**
     * 添加url到url容器
     * @param context context
     */
    public static void url(Context context) {
        log.debug("Mapping url {}{}", context.url(),config().getURLSuffix());
        Bored.INSTANCE.URL_MAP.put(context.url(), context);
    }

    /**
     * 根据 url 获取 Context 实例
     * @param url url
     * @return URL 实例
     */
    public static Optional<Context> url(String url) {
        var error = Bored.INSTANCE.URL_MAP.get("/error");
        return Optional.of(Bored.INSTANCE.URL_MAP.getOrDefault(url, error));
    }

    /**
     * 获取url列表
     * @return url列表
     */
    public static List<Context> urls() {
        return new ArrayList<>(Bored.INSTANCE.URL_MAP.values());
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
                    return;
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
