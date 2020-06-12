package com.bored.context;

/**
 * 上下文工厂-抽象工厂模式
 */
public interface ContextFactory {
    /**
     * 创建上下文对象
     * @return context
     */
    Context create();
}
