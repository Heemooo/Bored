package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class HTML {
    @Setter
    @Getter
    private String url;

    @Setter
    @Getter
    private String fullFilePath;

    @Setter
    @Getter
    private String content;

    private Context context;

    @Setter
    @Getter
    private String contentType;

    private Map<String, Object> ctx = new HashMap<>();

    public void setContext(Context context) {
        this.context = context;
        ctx.put("this", context);
    }

    public HTML add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    public String content() {
        content = Bored.of().getEnv().getJetTemplateHelper().parse(context.getTemplatePath(), ctx);
        return content;
    }

    public void out() {
        if(StrUtil.isEmpty(content)){
            content = this.content();
        }
        FileUtil.writeBytes(this.content.getBytes(CharsetUtil.CHARSET_UTF_8), fullFilePath);
    }
}
