package com.bored.model;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Page {
    private String title;

    private String createTime;

    private Boolean draft;

    private String type;

    private String layout;

    private String url;

    private String permLink;

    private List<String> tags;

    private List<String> categories;

    private String description;

    private String content;

    private List<String> toc;

    public String getType() {
        return Objects.isNull(type) ? StrUtil.EMPTY : type;
    }

    public String getLayout() {
        return Objects.isNull(type) ? "page" : type;
    }

    public Page(FrontMatter frontMatter) {
        this.title = frontMatter.getTitle();
        this.createTime = (Objects.isNull(frontMatter.getCreateTime())) ? frontMatter.getDate() : frontMatter.getCreateTime();
        this.draft = frontMatter.getDraft();
        this.type = frontMatter.getType();
        this.layout = frontMatter.getLayout();
        this.url = frontMatter.getUrl();
        this.tags = frontMatter.getTags();
        this.categories = frontMatter.getCategories();
        this.description = frontMatter.getDescription();
    }

    @Data
    public static class FrontMatter {
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

}
