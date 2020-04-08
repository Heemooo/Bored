package com.bored;

import cn.hutool.core.io.file.FileReader;
import lombok.SneakyThrows;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class H2Test {
    @Test
    @SneakyThrows
    public void connect() {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:core", "sa", "");
        Statement stmt = connection.createStatement();
        var initSql = new FileReader("sql/init.sql");
        stmt.execute(initSql.readString());
        stmt.executeUpdate("INSERT INTO PAGE VALUES ('1','1','1', '1', '1', '1', '1', '1', '1', '1', '1')");
        //查询
        ResultSet rs = stmt.executeQuery("SELECT * FROM PAGE");
        //遍历结果集
        while (rs.next()) {
            System.out.println(rs.getString("title"));
        }
        //释放资源
        stmt.close();
    }
}
