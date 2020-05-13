package com.bored.template;

import com.bored.Bored;
import com.bored.model.Category;
import com.bored.model.Tag;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 注册方法:在任意模板页面都可以访问的方法合集
 */
public class JetTemplateFunction {

    /**
     * 获取tag列表
     * @return tag列表
     */
    public static List<Tag> tags() {
        return Bored.tags();
    }

    /**
     * 通过标签名获取tag对象
     * @param tagName 标签名
     * @return 标签对象
     */
    public static Tag tag(String tagName) {
        AtomicReference<Tag> target = new AtomicReference<>();
        tags().parallelStream().forEach(tag -> {
            if (tag.getName().equals(tagName)) {
                target.set(tag);
            }
        });
        return target.get();
    }

    /**
     * 通过标签名获取标签url
     * @param tagName 标签名
     * @return 标签url
     */
    public static String tagUrl(String tagName) {
        return tag(tagName).getUrl();
    }

    /**
     * 获取分类列表
     * @return 分类列表
     */
    public static List<Category> categories() {
        return Bored.categories();
    }

    /**
     * 通过分类名获取分类对象
     * @param categoryName 分类名
     * @return 分类对象
     */
    public static Category category(String categoryName) {
        AtomicReference<Category> target = new AtomicReference<>();
        categories().parallelStream().forEach(tag -> {
            if (tag.getName().equals(categoryName)) {
                target.set(tag);
            }
        });
        return target.get();
    }

    /**
     * 通过分类名获取分类url
     * @param categoryName 分类名
     * @return 分类url
     */
    public static String categoryUrl(String categoryName) {
        return category(categoryName).getUrl();
    }
}
