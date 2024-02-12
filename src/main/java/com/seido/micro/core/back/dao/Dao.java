package com.seido.micro.core.back.dao;

import java.util.List;

/**
 * Interface Dao
 * @param <T> Parameter type
 */
public interface Dao<T> {
    /**
     * Method save
     * @param t Parameter type
     */
    public void save(T t);

    /**
     * Method load
     * @param id Parameter identifier
     * @return Return T
     */
    public T load(long id);

    /**
     * Method delete
     * @param id Parameter identifier from object
     */
    public void delete(long id);

    /**
     * Method update
     * @param t Parameter type
     */
    public void update(T t);

    /**
     * Method load all
     * @return List T
     */
    public List<T> loadAll();
}
