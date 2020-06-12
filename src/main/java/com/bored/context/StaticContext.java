package com.bored.context;

/**
 * 静态资源上下文
 */
public class StaticContext extends AbstractContext {

    @Override
    public String contentType() {
        assert super.getContentType() != null;
        return super.getContentType();
    }

    @Override
    public byte[] bytes() {
        assert super.getBytes() != null && super.getBytes().length != 0;
        return super.getBytes();
    }
}
