package com.bored.db;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bored.Bored;
import com.bored.db.entity.Category;
import com.bored.db.entity.Tag;
import com.bored.model.Page;
import com.bored.util.PageUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Db {
    private static final String driver = "org.h2.Driver";
    //private static final String url = "jdbc:h2:mem:core";
    private static final String url = "jdbc:h2:~/test";
    private static final String user = "sa";
    private static final String password = StrUtil.EMPTY;

    private Db() {
    }

    private static class DbHolder {
        private final static Db db = new Db();
    }

    private static Db of() {
        return DbHolder.db;
    }

    @SneakyThrows
    private Connection getConnection() {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }


    @SneakyThrows
    public static void init() {
        Connection connection = Db.of().getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        var initSql = new FileReader("sql/init.sql");
        stmt.execute(initSql.readString());
    }

    @SneakyThrows
    public static void loadData() {
        var env = Bored.of().getEnv();
        PageUtil pageUtil = new PageUtil(env.getRoot(), env.getSiteConfig());
        List<Page> pages = pageUtil.parse();
        Connection connection = Db.of().getConnection();
        connection.setAutoCommit(false);
        pages.forEach(page -> {
            Integer pageId = insertPage(page);
            List<String> category = page.getCategories();
            if (Objects.nonNull(category)) {
                category.forEach(name -> {
                    int categoryId = insertCategory(name);
                    String sql = "INSERT INTO PAGE_CATEGORY(PAGE_ID,CATEGORY_ID)VALUES(?,?)";
                    insert(sql, pageId, categoryId);
                });
            }
            List<String> tag = page.getTags();
            if (Objects.nonNull(tag)) {
                tag.forEach(name -> {
                    int tagId = insertTag(name);
                    String sql = "INSERT INTO PAGE_TAG(PAGE_ID,TAG_ID)VALUES(?,?)";
                    insert(sql, pageId, tagId);
                });
            }
        });
        connection.commit();
        connection.setAutoCommit(true);
    }

    @SneakyThrows
    public static Integer insertPage(Page page) {
        int id = 0;
        Console.log(page.getLayout());
        String sql = "INSERT INTO PAGE(TITLE,DATE,DRAFT,TYPE,LAYOUT,URL,PERM_LINK,DESCRIPTION,CONTENT,TOC)VALUES(?,?,?,?,?,?,?,?,?,?)";
        @Cleanup PreparedStatement statement = Db.of().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, page.getTitle());
        statement.setString(2, page.getDate());
        statement.setObject(3, page.isDraft());
        statement.setString(4, page.getType());
        statement.setString(5, page.getLayout());
        statement.setString(6, page.getUrl());
        statement.setString(7, page.getPermLink());
        statement.setString(8, page.getDescription());
        statement.setString(9, page.getContent());
        statement.setString(10, JSONUtil.parseArray(page.getToc()).toString());
        statement.executeUpdate();
        @Cleanup ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    @SneakyThrows
    public static Integer insertTag(String name) {
        String sql = "select ID from TAG where NAME = ?";
        @Cleanup PreparedStatement statement = Db.of().getConnection().prepareStatement(sql);
        statement.setString(1, name);
        int id = 0;
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        } else {
            sql = "insert into TAG(NAME) VALUES ( ? )";
            statement = Db.of().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        return id;
    }

    @SneakyThrows
    public static Integer insertCategory(String name) {
        String sql = "select ID from CATEGORY where NAME = ?";
        @Cleanup PreparedStatement statement = Db.of().getConnection().prepareStatement(sql);
        statement.setString(1, name);
        int id = 0;
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        } else {
            sql = "insert into CATEGORY(NAME) VALUES ( ? )";
            statement = Db.of().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        return id;
    }

    @SneakyThrows
    public static void insert(String sql, Object... params) {
        @Cleanup PreparedStatement statement = Db.of().getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        statement.executeUpdate();
    }

    @SneakyThrows
    public static Map<String, Page> getPages() {
        List<Page> pages = select("SELECT *FROM PAGE", Page.class);
        Map<String, Page> pageMap = new HashMap<>(pages.size());
        pages.forEach(page -> {
            List<Tag> tagList = select("SELECT TAG.ID,TAG.NAME FROM TAG INNER JOIN PAGE_TAG ON TAG.ID = PAGE_TAG.TAG_ID WHERE PAGE_ID = ?", Tag.class, page.getId());
            page.setTags(tagList.stream().map(Tag::getName).collect(Collectors.toList()));

            List<Category> categoryList = select("SELECT CATEGORY.ID,CATEGORY.NAME FROM CATEGORY INNER JOIN PAGE_CATEGORY ON CATEGORY.ID = PAGE_CATEGORY.CATEGORY_ID WHERE PAGE_ID = ?", Category.class, page.getId());
            page.setCategories(categoryList.stream().map(Category::getName).collect(Collectors.toList()));
            pageMap.put(page.getUrl(), page);
            if (!page.getPermLink().equals(page.getUrl())) {
                pageMap.put(page.getPermLink(), page);
            }
        });
        return pageMap;
    }



    @SneakyThrows
    public static <T> List<T> select(String sql,Class<T> zClass, Object... params) {
        @Cleanup  PreparedStatement statement = Db.of().getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        @Cleanup ResultSet resultSet = statement.executeQuery();
        return ResultSetUtil.toObject(resultSet, zClass);
    }

    @SneakyThrows
    public static void main(String[] args) {
        String sql = "select *from page";
        Connection connection = Db.of().getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        @Cleanup ResultSet resultSet = stmt.executeQuery(sql);
        List<Page> pageEntities = ResultSetUtil.toObject(resultSet, Page.class);
        pageEntities.forEach(pageEntity -> Console.log(pageEntity.getContent()));
    }
}
