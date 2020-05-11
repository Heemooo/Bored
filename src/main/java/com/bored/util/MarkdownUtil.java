package com.bored.util;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.SneakyThrows;


public class MarkdownUtil {
    /**
     * markdown文档转html内容
     */
    @SneakyThrows
    public static String markdown2Html(String content){
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }
}
