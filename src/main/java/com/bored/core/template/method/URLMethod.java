package com.bored.core.template.method;

import com.bored.Bored;

/**
 * 注册方法:对某些实例方法拓展
 */
public class URLMethod {

    /**
     * 返回绝对路径 @example ${url.absLangURL()}
     * @param url blog/
     * @return https://example.com/hugo/en/blog/
     */
    public static String absLangURL(String url) {
        return Bored.config().getBaseURL() + url;
    }

    /**
     * 返回相对路径 @example ${url.relLangURL()}
     * @param url blog/
     * @return /hugo/en/blog/
     */
    public static String relLangURL(String url) {
        return url;
    }
}
