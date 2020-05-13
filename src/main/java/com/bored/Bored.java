package com.bored;

import com.bored.core.Page;
import com.bored.core.Pages;
import com.bored.core.Paths;
import com.bored.core.Site;
import com.bored.core.command.Command;
import com.bored.model.Category;
import com.bored.model.Tag;
import com.bored.template.JetTemplateHelper;
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

    private Bored() {
    }

    private static class BoredHolder {
        private static final Bored INSTANCE = new Bored();
    }

    private static Bored of() {
        return BoredHolder.INSTANCE;
    }

    public static void config(Site config) {
        Bored.of().config = config;
        jetTemplateHelper(new JetTemplateHelper(Paths.layoutPath(config.getTheme())));
        jetTemplateConfig(Site.class, "site", config);
    }

    public static Site config() {
        return Bored.of().config;
    }

    public static void jetTemplateHelper(JetTemplateHelper helper) {
        Bored.of().jetTemplateHelper = helper;
    }

    public static JetTemplateHelper jetTemplateHelper() {
        return Bored.of().jetTemplateHelper;
    }

    public static void jetTemplateConfig(Class<?> clazz, String name, Object object) {
        Bored.of().jetTemplateHelper.getEngine().getGlobalContext().set(clazz, name, object);
    }

    public static void tag(Tag tag) {
        Bored.of().TAG_LIST.add(tag);
    }

    public static List<Tag> tags() {
        return Bored.of().TAG_LIST;
    }

    public static void category(Category category) {
        Bored.of().CATEGORY_LIST.add(category);
    }

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
        List<Page> pageList = Pages.sortByDate(Bored.of().PAGE_MAPS.get(type));
        for (int i = 0, len = pageList.size(); i < len; i++) {
            if (i < (len - 1)) pageList.get(i).setNext(pageList.get(i + 1));
            if (i > 0) pageList.get(i).setPrev(pageList.get(i - 1));
        }
        return pageList;
    }

    /**
     * 获取所有文章
     * @return 文章列表
     */
    public static List<Page> pages() {
        return Bored.of().PAGE_MAPS.values().stream().reduce(new ArrayList<>(), (t1, t2) -> {
            t1.addAll(t2);
            return t1;
        }).stream().sorted(Comparator.comparing(Page::getDate).reversed()).collect(Collectors.toList());
    }

    /**
     * 获取文章合集
     * @return 文章合集
     */
    public static Map<String, List<Page>> pageMaps() {
        return Bored.of().PAGE_MAPS;
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
        System.out.println("Unknown command " + command + ".");
        Command.displayHelp();
    }

}
