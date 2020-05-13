package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import com.bored.core.model.Context;
import com.bored.util.Paths;
import lombok.Builder;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Builder
public class URL {

    private final String uri;

    private String outPutPath;

    private final String filePath;

    private final Context context;

    private final String contentType;

    private final Map<String, Object> ctx = new HashMap<>();

    public String uri(){
        return uri;
    }

    public String contentType() {
        return contentType;
    }

    public BufferedInputStream getInputStream() {
        if (StrUtil.isEmpty(filePath)) return null;
        return new FileReader(filePath).getInputStream();
    }

    public URL add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    public String content() {
        if (Objects.isNull(context)) {
            return null;
        } else {
            this.ctx.put("ctx", this.context);
            return Bored.jetTemplateHelper().parse(context.getTemplatePath(), this.ctx);
        }
    }

    public void out() {
        var content = content();
        this.outPutPath = Paths.convertCorrectPath(this.outPutPath);
        if (StrUtil.isEmpty(content)) {
            FileUtil.writeBytes(new FileReader(this.filePath).readBytes(), this.outPutPath);
        } else {
            FileUtil.writeBytes(content.getBytes(CharsetUtil.CHARSET_UTF_8), this.outPutPath);
        }
    }

    @Override
    public String toString() {
        return "URL{" +
                "uri='" + uri + '\'' +
                ", fullFilePath='" + outPutPath + '\'' +
                ", filePath='" + filePath + '\'' +
                ", context=" + context +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
