package com.bored.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import com.bored.Bored;
import com.bored.core.model.Context;
import com.bored.util.Paths;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class URL {

    private final String outPutPath;

    private final Context context;

    private final String contentType;

    private final byte[] bytes;

    private URL(Context context, String contentType, String outPutPath, byte[] bytes) {
        this.outPutPath = Paths.convertCorrectPath(outPutPath);
        this.context = context;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public static URL createHTMLURL(Context context, String outPutPath) {
        return URL.createDefaultURL(context, outPutPath, null);
    }

    public static URL createDefaultURL(Context context, String outPutPath, byte[] bytes) {
        var contentType = "text/html;charset=utf-8";
        return new URL(context, contentType, outPutPath, bytes);
    }

    public static URL createStaticURL(Context context, String contentType, String outPutPath, byte[] bytes) {
        return new URL(context, contentType, outPutPath, bytes);
    }

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
        var template = this.context().template() + ".html";
        return Bored.jetTemplateHelper().parse(template, this.ctx);
    }

    public void out() {
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
