package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.model.UserUrl;

import java.util.List;
import java.util.Optional;

public interface UserUrlRepository extends CrudRepository<UserUrl, Long>  {
    List<Long> findAllFirstEntityIdsBySecondEntityId(Long urlID);
    List<Long> findAllSecondEntityIdsByFirstEntityId(Long userID);
    void deleteAllByFirstEntityId(Long userID);
    void deleteAllBySecondEntityId(Long urlID);
    Optional<Long> getIdByPair(UserUrl userUrl);
}
