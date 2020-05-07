package com.bored.db.dao;

import com.bored.db.Db;
import com.bored.db.model.Page;

public class PageDAO implements BaseDAO<Page> {
    @Override
    public Page query(long id) {
        return Db.getDao().fetch(Page.class, id);
    }

    @Override
    public long update(Page page) {
        return Db.getDao().update(page);
    }

    @Override
    public long delete(long id) {
        return Db.getDao().delete(Page.class, id);
    }

    @Override
    public long insert(Page page) {
        return Db.getDao().insert(page);
    }
}
