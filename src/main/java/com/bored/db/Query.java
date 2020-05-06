package com.bored.db;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.db.entity.Category;
import com.bored.db.entity.Tag;
import com.bored.db.entity.Page;
import com.bored.util.PageUtil;
import com.bored.util.PathUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Query {

    @SneakyThrows
    public static void loadData() {
        var env = Bored.of().getEnv();
        PageUtil pageUtil = new PageUtil(env.getRoot(), env.getSiteConfig());
        List<Page> pages = pageUtil.parse();
        var dao = Db.getDao();
        dao.startTransaction();
       // Connection connection = Db.of().getConnection();
       // connection.setAutoCommit(false);
        pages.forEach(page -> {
            int id = dao.insert(page);
            List<String> category = page.getCategories();
            if (Objects.nonNull(category)) {
                category.forEach(name -> {
                    var categoryId = insertCategory(name);
                    insert(props.getStr("insert_page_category"), pageId, categoryId);
                });
            }
            List<String> tag = page.getTags();
            if (Objects.nonNull(tag)) {
                tag.forEach(name -> {
                    var tagId = insertTag(name);
                    insert(props.getStr("insert_page_tag"), pageId, tagId);
                });
            }
        });
        var themeRootPath = env.getThemePath();
        var staticPath = env.getStaticPath();
        var files = FileUtil.loopFiles(staticPath);
        for (File file : files) {
            var url = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), themeRootPath));
            insert(props.getStr("insert_static_resource"), url, file.getPath());
        }
        connection.commit();
        connection.setAutoCommit(true);
    }


    public static long insertTag(String name) {
        long id;
        List<Tag> tags = select(props.getStr("select_tag"), Tag.class, name);
        if (tags.isEmpty()) {
            id = insert(props.getStr("insert_tag"), name);
        } else {
            Tag tag = tags.get(0);
            id = tag.getId();
        }
        return id;
    }


    public static long insertCategory(String name) {
        long id;
        List<Category> categories = select(props.getStr("select_category"), Category.class, name);
        if (categories.isEmpty()) {
            id = insert(props.getStr("insert_category"), name);
        } else {
            Category category = categories.get(0);
            id = category.getId();
        }
        return id;
    }

    public static Map<String, Page> getPages() {
        List<Page> pages = select(props.getStr("select_page") + " ORDER BY DATE", Page.class);
        Map<String, Page> pageMap = new HashMap<>(pages.size());
        pages.forEach(page -> {
            List<Tag> tagList = select(props.getStr("select_tags"), Tag.class, page.getId());
            page.setTags(tagList.stream().map(Tag::getName).collect(Collectors.toList()));

            List<Category> categoryList = select(props.getStr("select_categories"), Category.class, page.getId());
            page.setCategories(categoryList.stream().map(Category::getName).collect(Collectors.toList()));

            pageMap.put(page.getUrl(), page);
            if (!page.getPermLink().equals(page.getUrl())) {
                pageMap.put(page.getPermLink(), page);
            }
        });
        return pageMap;
    }

    public static Map<String, String> getStaticResource() {
        //List<StaticResource> resources = select(props.getStr("select_static_resource"), StaticResource.class);
        //Map<String, String> pageMap = new HashMap<>(resources.size());
        //resources.forEach(resource -> pageMap.put(resource.getUri(), resource.getFilePath()));
        return null;
    }


    @SneakyThrows
    public static void main(String[] args) {
        /*String sql = "select *from page";
        Connection connection = Db.of().getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        @Cleanup ResultSet resultSet = stmt.executeQuery(sql);
        List<Page> pageEntities = ResultSetUtil.toObject(resultSet, Page.class);
        pageEntities.forEach(pageEntity -> Console.log("id={},date={}", pageEntity.getId(), pageEntity.getDate()));*/

    }
}
