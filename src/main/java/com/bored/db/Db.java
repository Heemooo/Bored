package com.bored.db;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.schoolwow.quickdao.QuickDAO;
import cn.schoolwow.quickdao.dao.DAO;
import com.bored.Bored;
import com.bored.db.entity.Category;
import com.bored.db.entity.StaticResource;
import com.bored.db.entity.Tag;
import com.bored.db.entity.User;
import com.bored.model.Page;
import com.bored.util.PageUtil;
import com.bored.util.PathUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Db {
    private static Props props;
    private static final String driver = "org.h2.Driver";
    private static final String url = "jdbc:h2:mem:core";
    //private static final String url = "jdbc:h2:~/test";
    private static final String user = "sa";
    private static final String password = StrUtil.EMPTY;

    private Db() {
        props = new Props("sql.properties");
        props.setProperty("sql", "sql");
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
    private static void setParams(PreparedStatement statement, Object... params) {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    @SneakyThrows
    public static int insert(String sql, Object... params) {
        @Cleanup var statement = Db.of().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        setParams(statement, params);
        int id = 0;
        statement.executeUpdate();
        @Cleanup var resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        return id;
    }

    @SneakyThrows
    public static <T> List<T> select(String sql, Class<T> zClass, Object... params) {
        @Cleanup var statement = Db.of().getConnection().prepareStatement(sql);
        setParams(statement, params);
        @Cleanup var resultSet = statement.executeQuery();
        return ResultSetUtil.toObject(resultSet, zClass);
    }

    @SneakyThrows
    public static int updateOrDelete(String sql, Object... params) {
        @Cleanup var statement = Db.of().getConnection().prepareStatement(sql);
        setParams(statement, params);
        return statement.executeUpdate();
    }


    @SneakyThrows
    public static void loadData() {
        var env = Bored.of().getEnv();
        PageUtil pageUtil = new PageUtil(env.getRoot(), env.getSiteConfig());
        List<Page> pages = pageUtil.parse();
        Connection connection = Db.of().getConnection();
        connection.setAutoCommit(false);
        pages.forEach(page -> {
            Integer pageId = insert(props.getStr("insert_page"), page.getTitle(), page.getDate(), page.isDraft(), page.getType(), page.getLayout(), page.getUrl(), page.getPermLink(), page.getDescription(), page.getContent());
            List<String> category = page.getCategories();
            if (Objects.nonNull(category)) {
                category.forEach(name -> {
                    int categoryId = insertCategory(name);
                    insert(props.getStr("insert_page_category"), pageId, categoryId);
                });
            }
            List<String> tag = page.getTags();
            if (Objects.nonNull(tag)) {
                tag.forEach(name -> {
                    int tagId = insertTag(name);
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


    public static Integer insertTag(String name) {
        int id;
        List<Tag> tags = select(props.getStr("select_tag"), Tag.class, name);
        if (tags.isEmpty()) {
            id = insert(props.getStr("insert_tag"), name);
        } else {
            Tag tag = tags.get(0);
            id = tag.getId();
        }
        return id;
    }


    public static Integer insertCategory(String name) {
        int id;
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
        List<StaticResource> resources = select(props.getStr("select_static_resource"), StaticResource.class);
        Map<String, String> pageMap = new HashMap<>(resources.size());
        resources.forEach(resource -> pageMap.put(resource.getUri(), resource.getFilePath()));
        return pageMap;
    }


    @SneakyThrows
    public static void main(String[] args) {
        /*String sql = "select *from page";
        Connection connection = Db.of().getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        @Cleanup ResultSet resultSet = stmt.executeQuery(sql);
        List<Page> pageEntities = ResultSetUtil.toObject(resultSet, Page.class);
        pageEntities.forEach(pageEntity -> Console.log("id={},date={}", pageEntity.getId(), pageEntity.getDate()));*/
        BasicDataSource mysqlDataSource = new BasicDataSource();
        mysqlDataSource.setDriverClassName(driver);
        mysqlDataSource.setUrl(url);
        mysqlDataSource.setUsername(user);
        mysqlDataSource.setPassword(password);
        QuickDAO quickDAO = QuickDAO.newInstance().dataSource(mysqlDataSource).packageName("com.bored.db.entity");
        DAO dao = quickDAO.build();
        //dao.create(User.class);
        var user = new User();
        user.setId(1);
        user.setPassword("passwd");
        user.setUsername("username");
        dao.insert(user);
        var data = dao.fetch(User.class, 1);
        Console.log(data);
    }
}
