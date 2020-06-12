package com.bored.context;

public class StaticContextFactory implements ContextFactory {

    private final Context context;

    public StaticContextFactory(String url, String contentType, byte[] bytes, String outputPath) {
        var context = new StaticContext();
        context.setUrl(url);
        context.setContentType(contentType);
        context.setBytes(bytes);
        context.setOutputPath(outputPath);
        this.context = context;
    }

    @Override
    public Context create() {
        return context;
    }
}
