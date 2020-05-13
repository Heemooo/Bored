package com.bored.util;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.bored.core.Paths;
import com.moandjiezana.toml.Toml;

import java.io.File;

public final class TomlUtil {

    /**
     * @param path 配置文件路径
     * @param t    配置文件对应实体类
     * @param <T>  配置文件泛型
     * @return 配置文件实例
     */
    public static <T> T loadTomlFile(String path, Class<T> t) {
        var toml = new Toml();
        var root = Paths.convertCorrectPath(path);
        toml.read(FileUtil.file(root));
        return toml.to(t);
    }

    public static <T> T tomlToObj(String tomlString, Class<T> tClass) {
        var toml = new Toml();
        toml.read(tomlString);
        return toml.to(tClass);
    }

}
