package com.bored.db.dao;

public interface BaseDAO<T> {
    T query(long id);

    long update(T t);

    long delete(long id);

    long insert(T t);
}
