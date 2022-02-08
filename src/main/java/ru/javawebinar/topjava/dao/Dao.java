package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T,V> {
    T create (T t);
    T read (V id);
    T update (T t);
    boolean delete (V id);
    List<T> getAll();
}
