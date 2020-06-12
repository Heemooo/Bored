package com.bored.context;

public class DefaultContextFactory implements ContextFactory {

    private final Context context;

    public DefaultContextFactory(String url, String type, String layout) {
        var context = new DefaultContext();
        context.setUrl(url);
        context.setType(type);
        context.setLayout(layout);
        this.context = context;
    }

    @Override
    public Context create() {
        return context;
    }
}
