package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Long> {
    Optional<Url> findByPath(String path);
    void deleteByIdAndUserId(Long id, Long userId);
}
