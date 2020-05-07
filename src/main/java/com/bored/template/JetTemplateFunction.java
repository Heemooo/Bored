package com.bored.template;

import cn.hutool.setting.dialect.Props;
import com.bored.model.Category;
import com.bored.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class JetTemplateFunction {

    private static final Props props = new Props("sql.properties");

    public static List<Tag> tags() {
        return null;
    }

    public static List<Category> categories() {
        return new ArrayList<>();
    }

}
