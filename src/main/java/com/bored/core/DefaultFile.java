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
    private DefaultFileContent content;

    public DefaultFile setFilePath(String filePath) {
        this.filePath = Bored.replaceSlash(filePath);
        return this;
    }

    public String getContent() {
        return content.content.toString();
    }

    public DefaultFile addLine(String line) {
        this.content.addLine(line);
        return this;
    }

    public interface DefaultFileInit {
        void init(DefaultFile defaultFile);
    }

    static class DefaultFileContent {
        StringBuilder content = new StringBuilder();

        private void addLine(String line) {
            content.append(line).append(System.getProperty("line.separator"));
        }
    }
}
