package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
