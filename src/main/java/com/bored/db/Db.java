package com.bored.db;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.schoolwow.quickdao.QuickDAO;
import cn.schoolwow.quickdao.dao.DAO;
import org.apache.commons.dbcp.BasicDataSource;

public class Db {
    private static Props props;
    private static final String driver = "org.h2.Driver";
    private static final String url = "jdbc:h2:mem:core";
    //private static final String url = "jdbc:h2:~/test";
    private static final String user = "sa";
    private static final String password = StrUtil.EMPTY;
    private final DAO dao;

    private Db() {
        BasicDataSource mysqlDataSource = new BasicDataSource();
        mysqlDataSource.setDriverClassName(driver);
        mysqlDataSource.setUrl(url);
        mysqlDataSource.setUsername(user);
        mysqlDataSource.setPassword(password);
        QuickDAO quickDAO = QuickDAO.newInstance().dataSource(mysqlDataSource).packageName("com.bored.db.entity");
        dao = quickDAO.build();
        props = new Props("sql.properties");
        props.setProperty("sql", "sql");
    }

    private static class DbHolder {
        private final static Db db = new Db();
    }

    private static Db of() {
        return DbHolder.db;
    }

    public static DAO getDao(){
        return Db.of().dao;
    }
}
