package com.mykhailopavliuk.repository.impl;

import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.repository.UserRepository;
import com.mykhailopavliuk.repository.UserUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Path usersDatabase;

    private final UserUrlRepository userUrlRepository;

    @Autowired
    public UserRepositoryImpl(@Qualifier("usersDatabase") Path usersDatabase, UserUrlRepository userUrlRepository) {
        this.usersDatabase = usersDatabase;
        this.userUrlRepository = userUrlRepository;
    }

    @Override
    public <S extends User> S save(S user) {
        Long userId = user.getId();
        if (!existsById(userId)) {
            try (var writer = Files.newBufferedWriter(usersDatabase, StandardOpenOption.APPEND)) {
                writer.append(String.valueOf(user.getId()));
                writer.append(",");
                writer.append(user.getEmail());
                writer.append(",");
                writer.append(String.valueOf(user.getPassword()));
                writer.append("\n");

            } catch (IOException e) {
                throw new DatabaseOperationException("Exception has occurred while writing to the database");
            }
        } else {
            String line;
            String[] userData;
            StringBuilder updatedFile = new StringBuilder();

            try (var reader = Files.newBufferedReader(usersDatabase)) {
                while ((line = reader.readLine()) != null) {
                    userData = line.split(",");

                    if (userData[0].equals(String.valueOf(userId))) {
                        updatedFile.append(userId);
                        updatedFile.append(",");
                        updatedFile.append(user.getEmail());
                        updatedFile.append(",");
                        updatedFile.append(String.valueOf(user.getPassword()));
                        updatedFile.append("\n");
                    } else {
                        updatedFile.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                throw new DatabaseOperationException("Exception has occurred while reading from the database");
            }

            try (var writer = Files.newBufferedWriter(usersDatabase)) {
                writer.write(updatedFile.toString());
            } catch (IOException e) {
                throw new DatabaseOperationException("Exception has while writing to the database");
            }
        }

        return user;
    }

    @Override
    public <S extends User> List<S> saveAll(List<S> users) {
        for (User user : users) {
            save(user);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String line;
        String[] userData;

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while ((line = reader.readLine()) != null) {
                userData = line.split(",");

                if (userData[0].equals(String.valueOf(id))) {
                    return Optional.of(new User(Long.parseLong(userData[0]), userData[1], userData[2].toCharArray(), null));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        String line;
        String[] userData;

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while ((line = reader.readLine()) != null) {
                userData = line.split(",");

                if (userData[0].equals(String.valueOf(id))) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return false;
    }

    @Override
    public List<User> findAll() {
        String line;
        String[] userData;
        List<User> users = new ArrayList<>();

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while ((line = reader.readLine()) != null) {
                userData = line.split(",");
                users.add(new User(Long.parseLong(userData[0]), userData[1], userData[2].toCharArray(), null));
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return users;
    }

    @Override
    public long count() {
        long count = 0;

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return count;
    }

    @Override
    public void deleteById(Long id) {
        String line;
        String[] userData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while ((line = reader.readLine()) != null) {
                userData = line.split(",");

                if (!userData[0].equals(String.valueOf(id))) {
                    updatedFile.append(line);
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        try (var writer = Files.newBufferedWriter(usersDatabase)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the database");
        }

        userUrlRepository.deleteAllByFirstEntityId(id);
    }

    @Override
    public void deleteAll() {
        try (var writer = Files.newBufferedWriter(usersDatabase)) {
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the database");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String line;
        String[] userData;

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while ((line = reader.readLine()) != null) {
                userData = line.split(",");

                if (userData[1].equals(email)) {
                    return Optional.of(new User(Long.parseLong(userData[0]), userData[1], userData[2].toCharArray(), null));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return Optional.empty();
    }

    @Override
    public long getAvailableId() {
        String line;
        String[] userData = null;

        try (var reader = Files.newBufferedReader(usersDatabase)) {
            while ((line = reader.readLine()) != null) {
                userData = line.split(",");
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return userData != null ? (Long.parseLong(userData[0]) + 1) : 1;
    }
}
