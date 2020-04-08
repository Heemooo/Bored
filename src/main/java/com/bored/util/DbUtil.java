package com.bored.util;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {

    private static final String driver = "org.h2.Driver";
    private static final String url = "jdbc:h2:mem:core";
    private static final String user = "sa";
    private static final String password = StrUtil.EMPTY;

    @SneakyThrows
    public static void init() {
        Connection connection = getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        var initSql = new FileReader("sql/init.sql");
        stmt.execute(initSql.readString());
        stmt.executeUpdate("INSERT INTO PAGE VALUES ('1','1','1', '1', '1', '1', '1', '1', '1', '1', '1')");
        System.out.println("初始化完成");
    }

    @SneakyThrows
    private static Connection getConnection() {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static List<Map<String, Object>> select(String sql) {
        Connection connection = getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        @Cleanup ResultSet resultSet = stmt.executeQuery(sql);
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), resultSet.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    @SneakyThrows
    public static void insert(String sql){
        Connection connection = getConnection();
        @Cleanup Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }
}
