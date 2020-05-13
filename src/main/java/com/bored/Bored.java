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

/**
 * @author https://gitee.com/heemooo
 * @since 2020/3/27
 */
@Slf4j
public final class Bored {

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
    private final Map<String, List<Tag>> TAG_MAPS = new HashMap<>();
    /**
     * 分类列表
     */
    private final Map<String, List<Category>> CATEGORY_MAPS = new HashMap<>();
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
        jetTemplateHelper(new JetTemplateHelper(Paths.LAYOUT_PATH));
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
        if (!Bored.of().TAG_MAPS.containsKey(tag.getType())) {
            Bored.of().TAG_MAPS.put(tag.getType(), new ArrayList<>());
        }
        Bored.of().TAG_MAPS.get(tag.getType()).add(tag);
    }

    public static List<Tag> tags(String type) {
        return Bored.of().TAG_MAPS.get(type);
    }

    public static void category(Category category) {
        if (!Bored.of().CATEGORY_MAPS.containsKey(category.getType())) {
            Bored.of().CATEGORY_MAPS.put(category.getType(), new ArrayList<>());
        }
        Bored.of().CATEGORY_MAPS.get(category.getType()).add(category);
    }

    public static List<Category> categories(String type) {
        return Bored.of().CATEGORY_MAPS.get(type);
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
