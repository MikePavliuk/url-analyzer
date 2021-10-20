package com.mykhailopavliuk.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    <S extends T> S save(S entity);

    <S extends T> List<S> saveAll(List<S> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    long count();

    void deleteById(ID id);

    void deleteAll();

    long getAvailableId();
}