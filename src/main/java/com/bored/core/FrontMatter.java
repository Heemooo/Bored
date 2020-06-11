
package com.bored.core;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.moandjiezana.toml.Toml;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;

@Data
public class FrontMatter {

    @Info("title")
    public String title = StrUtil.EMPTY;

    @Info("create time")
    public Date date;

    @Info("is draft")
    public boolean draft;

    @Info("url")
    private String url = StrUtil.EMPTY;

    @Info("")
    private String summary;

    @Info("")
    private String layout = "page.html";

    @Info("")
    private List<String> tags = new ArrayList<>();

    @Info("")
    private List<String> categories = new ArrayList<>();

    public Map<String, Object> toMap() {
        Field[] fields = ReflectUtil.getFields(this.getClass());
        Map<String, Object> params = new HashMap<>(fields.length);
        for (Field field : fields) {
            params.put(field.getName(), ReflectUtil.getFieldValue(this, field));
        }
        return params;
    }

    public static FrontMatter toObject(String tomlContent) {
        var toml = new Toml();
        toml.read(tomlContent);
        return toml.to(FrontMatter.class);
    }

}