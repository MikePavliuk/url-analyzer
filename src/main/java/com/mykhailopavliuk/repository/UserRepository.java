package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    long getAvailableId();
}
