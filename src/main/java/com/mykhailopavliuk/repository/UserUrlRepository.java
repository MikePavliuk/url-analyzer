package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.model.UserUrl;

import java.util.List;
import java.util.Optional;

public interface UserUrlRepository extends CrudRepository<UserUrl, Long> {
    List<Long> findAllUserIdsByUrlId(Long urlID);

    List<Long> findAllUrlIdsByUserId(Long userID);

    void deleteAllByUserId(Long userID);

    void deleteAllByUrlId(Long urlID);

    Optional<Long> getIdByPair(UserUrl userUrl);
}
