package com.bored.core.context;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public abstract class AbstractContext implements Context {

    /**
     * 上下文对应的url
     */
    private String url;
    /**
     * 类型，如果前面没有指定，此值将自动派生自目录
     */
    private String type;
    /**
     * 文章模板
     */
    private String layout;
    /**
     * 输出路径
     */
    private String outputPath;
    /**
     * contentType
     */
    private String contentType;
    /**
     * 内容
     */
    private byte[] bytes;

    /**
     * 上下文携带的参数
     */
    private final Map<String, Object> ctx = new HashMap<>();

    @Override
    public String url() {
        assert url != null;
        return this.url;
    }

    @Override
    public Context addObject(String key, Object object) {
        assert key != null;
        assert object != null;
        this.ctx.put(key, object);
        return this;
    }

    @Override
    public void out() {
        assert this.outputPath != null;
        assert this.bytes != null;
        FileUtil.writeBytes(this.bytes(), this.outputPath);
    }
}
