package com.bored.core;

import lombok.Data;

/**
 * 默认文件
 * @author https://gitee.com/heemooo
 * @since 2020/3/28
 */
@Data
public class DefaultFile {
    /**
     * 文件全路径
     */
    private String filePath;
    /**
     * 文件内容
     */
    private String content;

    public DefaultFile setFilePath(String filePath) {
        this.filePath = Bored.convertCorrectPath(filePath);
        return this;
    }

    public interface DefaultFileInit {
        void init(DefaultFile defaultFile);
    }
}
