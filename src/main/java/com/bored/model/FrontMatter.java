
package com.bored.model;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FrontMatter {
    public String title = StrUtil.EMPTY;
    public String createTime;
    public String date;
    public Boolean draft = Boolean.TRUE;
    private String url = StrUtil.EMPTY;
    private String type;
    private String layout;
    private List<String> tags;
    private List<String> categories;
    private String description;

    public Map<String, Object> toMap() {
        Field[] fields = ReflectUtil.getFields(this.getClass());
        Map<String, Object> params = new HashMap<>(fields.length);
        for (Field field : fields) {
            params.put(field.getName(), ReflectUtil.getFieldValue(this, field));
        }
        return params;
    }
}