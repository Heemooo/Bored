package com.bored.db;

import cn.hutool.setting.dialect.Props;
import cn.schoolwow.quickdao.QuickDAO;
import cn.schoolwow.quickdao.dao.DAO;
import org.apache.commons.dbcp.BasicDataSource;

public class Db {

    private final DAO dao;

    private Db() {
        Props props = new Props("db.properties");
        BasicDataSource mysqlDataSource = new BasicDataSource();
        String databaseType = props.getStr("database.type");
        mysqlDataSource.setDriverClassName(props.getStr("jdbc.driver." + databaseType));
        mysqlDataSource.setUrl(props.getStr("jdbc.url." + databaseType));
        mysqlDataSource.setUsername(props.getStr("jdbc.user." + databaseType));
        mysqlDataSource.setPassword(props.getStr("jdbc.password." + databaseType));
        QuickDAO quickDAO = QuickDAO.newInstance().dataSource(mysqlDataSource).packageName("com.bored.db.entity");
        dao = quickDAO.build();
    }

    private static class DbHolder {
        private final static Db db = new Db();
    }

    private static Db of() {
        return DbHolder.db;
    }

    public static DAO getDao() {
        return Db.of().dao;
    }

}
