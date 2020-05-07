package com.bored.template;

import cn.hutool.setting.dialect.Props;
import com.bored.db.entity.CategoryEntity;
import com.bored.db.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

public class JetTemplateFunction {

    private static final Props props = new Props("sql.properties");

    public static List<TagEntity> tags() {
        return null;
    }

    public static List<CategoryEntity> categories() {
        return new ArrayList<>();
    }

}
