package com.mykhailopavliuk.service;

import java.util.List;

public interface CrudService <T, ID> {
    T create(T entity);
    T readById(ID id);
    T update(T entity);
    void delete(ID id);
    List<T> getAll();
}
