package com.bored.template;

import cn.hutool.setting.dialect.Props;
import com.bored.db.Db;
import com.bored.db.entity.Category;
import com.bored.db.entity.Tag;
import jetbrick.template.JetAnnotations;

import java.util.ArrayList;
import java.util.List;

public class JetTemplateFunction {

    private static Props props = new Props("sql.properties");

    public static List<Tag> tags() {
        return Db.select(props.getStr("select_tags_count"), Tag.class);
    }

    public static List<Category> categories() {
        return new ArrayList<>();
    }

}
