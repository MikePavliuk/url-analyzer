package com.mykhailopavliuk.repository.impl;

import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.model.UserUrl;
import com.mykhailopavliuk.repository.UserUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserUrlRepositoryImpl implements UserUrlRepository {

    private final Path user_urlDatabase;

    @Autowired
    public UserUrlRepositoryImpl(@Qualifier("user_urlDatabase") Path user_urlDatabase) {
        this.user_urlDatabase = user_urlDatabase;
    }

    @Override
    public <S extends UserUrl> S save(S userUrl) {
        try (var writer = Files.newBufferedWriter(user_urlDatabase, StandardOpenOption.APPEND)) {
            writer.append(String.valueOf(userUrl.getId()));
            writer.append(",");
            writer.append(String.valueOf(userUrl.getUserId()));
            writer.append(",");
            writer.append(String.valueOf(userUrl.getUrlId()));
            writer.append("\n");

        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User-Url database");
        }

        return userUrl;
    }

    @Override
    public <S extends UserUrl> List<S> saveAll(List<S> userUrlList) {
        for (UserUrl userUrl : userUrlList) {
            save(userUrl);
        }
        return userUrlList;
    }

    @Override
    public Optional<UserUrl> findById(Long id) {
        String line;
        String[] recordData;

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (recordData[0].equals(String.valueOf(id))) {
                    return Optional.of(new UserUrl(Long.parseLong(recordData[0]), Long.parseLong(recordData[1]), Long.parseLong(recordData[2])));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        String line;
        String[] recordData;

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (recordData[0].equals(String.valueOf(id))) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return false;
    }

    @Override
    public List<UserUrl> findAll() {
        String line;
        String[] recordData;
        List<UserUrl> userUrlList = new ArrayList<>();

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");
                userUrlList.add(new UserUrl(Long.parseLong(recordData[0]), Long.parseLong(recordData[1]), Long.parseLong(recordData[2])));
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the database");
        }

        return userUrlList;
    }

    @Override
    public long count() {
        long count = 0;

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return count;
    }

    @Override
    public void deleteById(Long id) {
        String line;
        String[] recordData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (!recordData[0].equals(String.valueOf(id))) {
                    updatedFile.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        try (var writer = Files.newBufferedWriter(user_urlDatabase)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User-Url database");
        }
    }

    @Override
    public void deleteAll() {
        try (var writer = Files.newBufferedWriter(user_urlDatabase)) {
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User-Url database");
        }
    }

    @Override
    public long getAvailableId() {
        String line;
        String[] recordData = null;

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return recordData != null ? (Long.parseLong(recordData[0]) + 1) : 1;
    }

    @Override
    public List<Long> findAllUserIdsByUrlId(Long urlID) {
        String line;
        String[] recordData;
        List<Long> usersIds = new ArrayList<>();

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (recordData[2].equals(String.valueOf(urlID))) {
                    usersIds.add(Long.parseLong(recordData[1]));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return usersIds;
    }

    @Override
    public List<Long> findAllUrlIdsByUserId(Long userID) {
        String line;
        String[] recordData;
        List<Long> urlsIds = new ArrayList<>();

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (recordData[1].equals(String.valueOf(userID))) {
                    urlsIds.add(Long.parseLong(recordData[2]));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return urlsIds;
    }

    @Override
    public void deleteAllByUserId(Long userID) {
        String line;
        String[] recordData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (!recordData[1].equals(String.valueOf(userID))) {
                    updatedFile.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        try (var writer = Files.newBufferedWriter(user_urlDatabase)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User-Url database");
        }
    }

    @Override
    public void deleteAllByUrlId(Long urlID) {
        String line;
        String[] recordData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (!recordData[2].equals(String.valueOf(urlID))) {
                    updatedFile.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        try (var writer = Files.newBufferedWriter(user_urlDatabase)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the User-Url database");
        }
    }

    @Override
    public Optional<Long> getIdByPair(UserUrl userUrl) {
        String line;
        String[] recordData;

        try (var reader = Files.newBufferedReader(user_urlDatabase)) {
            while ((line = reader.readLine()) != null) {
                recordData = line.split(",");

                if (recordData[1].equals(String.valueOf(userUrl.getUserId())) && recordData[2].equals(String.valueOf(userUrl.getUrlId()))) {
                    return Optional.of(Long.parseLong(recordData[0]));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the User-Url database");
        }

        return Optional.empty();
    }
}
