package com.bored.db;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.db.entity.*;
import com.bored.db.model.Page;
import com.bored.util.PageUtil;
import com.bored.util.PathUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Query {

    @SneakyThrows
    public static void loadData() {
        var env = Bored.of().getEnv();
        PageUtil pageUtil = new PageUtil();
        List<Page> pages = pageUtil.loadPages();
        var dao = Db.getDao();
        dao.startTransaction();
        pages.forEach(page -> {
            var pageEntity = new PageEntity();
            BeanUtil.copyProperties(page, pageEntity);
            long id = dao.insert(pageEntity);
            List<String> categories = page.getCategories();
            if (Objects.nonNull(categories)) {
                categories.forEach(name -> {
                    var category = new CategoryEntity();
                    category.setName(name);
                    var categoryId = dao.insert(category);
                    var pageCategory = new PageCategory(id, categoryId);
                    dao.insert(pageCategory);
                });
            }
            List<String> tags = page.getTags();
            if (Objects.nonNull(tags)) {
                tags.forEach(name -> {
                    var tag = new TagEntity();
                    tag.setName(name);
                    var tagId = dao.insert(tag);
                    var pageTag = new PageTag(id, tagId);
                    dao.insert(pageTag);
                });
            }
        });
        var themeRootPath = env.getThemePath();
        var staticPath = env.getStaticPath();
        var files = FileUtil.loopFiles(staticPath);
        for (File file : files) {
            var url = PathUtil.convertCorrectUrl(StrUtil.removePrefix(file.getPath(), themeRootPath));
            // insert(props.getStr("insert_static_resource"), url, file.getPath());
        }
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
