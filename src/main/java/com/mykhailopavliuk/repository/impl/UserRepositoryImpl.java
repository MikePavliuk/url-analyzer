package com.mykhailopavliuk.repository.impl;

import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public <S extends User> S save(S user) {
        try (var writer = new BufferedWriter(new FileWriter("database.csv", true))) {
            writer.append(String.valueOf(user.getId()));
            writer.append(",");
            writer.append(user.getEmail());
            writer.append(",");
            writer.append(String.valueOf(user.getPassword()));
            writer.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }
}