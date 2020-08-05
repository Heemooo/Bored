package com.bored.util;

import com.bored.constant.Constants;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param filepath 当前遍历的目录
     * @return 文件列表
     */
    public static List<Path> loopFiles(String filepath) {
        if (filepath == null || filepath.isBlank()) {
            return Collections.emptyList();
        }
        List<Path> paths = new ArrayList<>();
        Path path = Paths.get(filepath);
        try {
            Files.walkFileTree(path, new FindJavaVisitor(paths));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public static String readString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_STR;
    }

    public static byte[] readBytes(Path path) {
        byte[] bytes = {};
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static List<String> readLines(Path path) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static class FindJavaVisitor extends SimpleFileVisitor<Path> {

        private final List<Path> result;

        public FindJavaVisitor(List<Path> result) {
            this.result = result;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            result.add(file);
            return FileVisitResult.CONTINUE;
        }
    }

    public static String contentType(Path path) {
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (contentType == null || contentType.isBlank()) ? "application/octet-stream" : contentType;
    }

    public static void main(String[] args) {
        String filepath = "D:\\DevPs\\Bored\\site-demo1";
        loopFiles(filepath).forEach(path -> {
            System.out.println(path.toString());
            System.out.println(path.getFileName().toString());
            System.out.println(contentType(path));
        });
    }
}
