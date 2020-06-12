package com.bored.core.context;

import com.bored.core.ContentType;

public interface Context {

    /**
     * 上下文对应的url
     * @return url
     */
    String url();

    /**
     * 返回Content-Type 的值
     * @return Content-Type
     */
    default String contentType() {
        return ContentType.TEXT_HTML;
    }

    /**
     * 上下文内容，将内容转换成了byte数组
     * @return byte[]
     */
    byte[] bytes();

    /**
     * 为上下文注册 变量
     * @param key    变量名
     * @param object 变量实例
     * @return 上下文
     */
    Context addObject(String key, Object object);

    /**
     * 输出上下文
     */
    void out();

}
