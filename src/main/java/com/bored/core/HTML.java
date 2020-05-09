package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import com.bored.Bored;

import java.util.HashMap;
import java.util.Map;

public class HTML {
    private String url;
    private String fullFilePath;
    private Context context;
    private Map<String, Object> ctx = new HashMap<>();

    private HTML add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    private String content() {
        return Bored.of().getEnv().getJetTemplateHelper().parse(context.getTemplatePath(), ctx);
    }

    private void out() {
        FileUtil.writeBytes(this.content().getBytes(CharsetUtil.CHARSET_UTF_8), fullFilePath);
    }
}
