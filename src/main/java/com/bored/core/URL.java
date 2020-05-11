package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.util.PathUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Builder
public class URL {

    public final String uri;

    private String fullFilePath;

    private final String filePath;

    private Context context;

    private final String contentType;

    private final Map<String, Object> ctx = new HashMap<>();

    public String contentType(){
        return contentType;
    }

    public BufferedInputStream getInputStream() {
        if (StrUtil.isEmpty(filePath)) return null;
        return new FileReader(filePath).getInputStream();
    }

    public void setContext(Context context) {
        this.context = context;
        ctx.put("ctx", context);
    }

    public URL add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    public String content() {
        if (Objects.isNull(context)) return null;
        else return Bored.env().getJetTemplateHelper().parse(context.getTemplatePath(), this.ctx);
    }

    public void out() {
        var content = content();
        this.fullFilePath = PathUtil.convertCorrectPath(this.fullFilePath);
        if (StrUtil.isEmpty(content)) {
            FileUtil.writeBytes(new FileReader(this.filePath).readBytes(), this.fullFilePath);
        } else {
            FileUtil.writeBytes(content.getBytes(CharsetUtil.CHARSET_UTF_8), this.fullFilePath);
        }
    }
}
