package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import com.bored.Bored;
import com.bored.core.model.Context;
import com.bored.util.Paths;
import lombok.Builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Builder
public class URL {

    private String outPutPath;

    private final Context context;

    private final String contentType;

    private final byte[] bytes;

    private final Map<String, Object> ctx = new HashMap<>();

    public Context context() {
        return this.context;
    }

    public String contentType() {
        return contentType;
    }

    public URL add(String key, Object object) {
        ctx.put(key, object);
        return this;
    }

    public String content() {
        this.ctx.put("ctx", this.context());
        return Bored.jetTemplateHelper().parse(this.context().template(), this.ctx);
    }

    public void out() {
        this.outPutPath = Paths.convertCorrectPath(this.outPutPath);
        FileUtil.writeBytes(this.bytes(), this.outPutPath);
    }

    public byte[] bytes() {
        if (this.bytes != null && this.bytes.length != 0) {
            return this.bytes;
        }
        return content().getBytes(CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public String toString() {
        return "URL{" +
                "outPutPath='" + outPutPath + '\'' +
                ", context=" + context +
                ", contentType='" + contentType + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", ctx=" + ctx +
                '}';
    }
}
