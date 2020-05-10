package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class URL {
    @Setter
    @Getter
    private String uri;

    @Setter
    private String fullFilePath;

    @Setter
    private String filePath;

    @Getter
    private Context context;

    @Setter
    @Getter
    private String contentType = "text/html;charset=utf-8";

    @Getter
    private Map<String, Object> ctx = new HashMap<>();

    public BufferedInputStream getInputStream() {
        if (StrUtil.isEmpty(filePath)) return null;
        return new FileReader(filePath).getInputStream();
    }

    public void setContext(Context context) {
        this.context = context;
        ctx.put("this", context);
    }

    public URL add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    public String content() {
        if (Objects.isNull(context)) return null;
        else return Bored.of().env().getJetTemplateHelper().parse(context.getTemplatePath(), this.getCtx());
    }

    public void out() {
        var content = content();
        if (StrUtil.isEmpty(content)) {
            FileUtil.writeBytes(new FileReader(this.filePath).readBytes(), this.fullFilePath);
        } else {
            FileUtil.writeBytes(content.getBytes(CharsetUtil.CHARSET_UTF_8), this.fullFilePath);
        }
    }
}
