package com.bored.core.context;

public class DefaultContextFactory implements ContextFactory {

    private final Context context;

    public DefaultContextFactory(String url, String type, String layout, String outputPath) {
        var context = new DefaultContext();
        context.setUrl(url);
        context.setType(type);
        context.setLayout(layout);
        context.setOutputPath(outputPath);
        this.context = context;
    }

    @Override
    public Context create() {
        return context;
    }
}
