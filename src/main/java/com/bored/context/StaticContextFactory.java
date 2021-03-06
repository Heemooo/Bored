package com.bored.context;

public class StaticContextFactory implements ContextFactory {

    private final Context context;

    public StaticContextFactory(String url, String contentType, byte[] bytes) {
        var context = new StaticContext();
        context.setUrl(url);
        context.setContentType(contentType);
        context.setBytes(bytes);
        this.context = context;
    }

    @Override
    public Context create() {
        return context;
    }
}
