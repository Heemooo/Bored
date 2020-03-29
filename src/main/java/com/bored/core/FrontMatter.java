package com.bored.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
public class FrontMatter {
    private String title = StrUtil.EMPTY;
    private String date = DateUtil.now();
    private Boolean draft = Boolean.TRUE;
    private String url = StrUtil.EMPTY;

    public Map<String, Object> toMap() {
        Field[] fields = ReflectUtil.getFields(FrontMatter.class);
        Map<String, Object> params = new HashMap<>(fields.length);
        for (Field field : fields) {
            params.put(field.getName(), ReflectUtil.getFieldValue(this, field));
        }
        return params;
    }
}
