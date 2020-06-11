package com.bored.core.model;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 当前页面的上下文对象
 */
@Getter
@Builder
public class Context {
    /**
     * 当前页面标题
     */
    private final String title;
    /**
     * 当前页面的url
     */
    private final String url;
    /**
     * 类型，如果前面没有指定，此值将自动派生自目录
     */
    private final String type;
    /**
     * 文章模板
     */
    private final String layout;
    /**
     * 时间
     */
    private final Date date;
    /**
     * 输出路径
     */
    private final String outPutPath;
    /**
     * contentType
     */
    private final String contentType;
    /**
     * 内容
     */
    private final byte[] bytes;
    /**
     * 上下文携带的参数
     */
    private final Map<String, Object> ctx = new HashMap<>();

    public Context addObject(String key, Object object) {
        assert key != null;
        assert object != null;
        this.ctx.put(key, object);
        return this;
    }

    public void out() {
        assert this.outPutPath != null;
        assert this.bytes != null;
        FileUtil.writeBytes(this.bytes(), this.outPutPath);
    }

    public byte[] bytes() {
        if (this.bytes != null && this.bytes.length != 0) {
            return this.bytes;
        }

        this.ctx.put("title", this.getTitle());
        this.ctx.put("url", this.getUrl());

        assert StrUtil.isNotBlank(this.getType());
        this.ctx.put("type", this.getType());
        this.ctx.put("layout", this.getLayout());

        var template = StrUtil.isBlank(this.getType()) ? this.getLayout() :
                this.getType() + "/" + this.getLayout();

        assert StrUtil.isNotBlank(template);
        var content = Bored.jetTemplateHelper().parse(template, this.ctx);
        return content.getBytes(CharsetUtil.CHARSET_UTF_8);
    }

}
