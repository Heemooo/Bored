package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
public class HTML {
    private String url;
    private String fullFilePath;
    private Context context;
    private String content;
    private Map<String, Object> ctx = new HashMap<>();

    public void setContext(Context context) {
        this.context = context;
        ctx.put("this", context);
    }

    private HTML add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    private String content() {
        content = Bored.of().getEnv().getJetTemplateHelper().parse(context.getTemplatePath(), ctx);
        return content;
    }

    private void out() {
        if(StrUtil.isEmpty(content)){
            content = this.content();
        }
        FileUtil.writeBytes(this.content.getBytes(CharsetUtil.CHARSET_UTF_8), fullFilePath);
    }
}
