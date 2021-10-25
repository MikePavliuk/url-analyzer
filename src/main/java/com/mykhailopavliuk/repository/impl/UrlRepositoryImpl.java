package com.mykhailopavliuk.repository.impl;

import com.mykhailopavliuk.exception.DatabaseOperationException;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.UserUrl;
import com.mykhailopavliuk.repository.UrlRepository;
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
public class UrlRepositoryImpl implements UrlRepository {

    private final Path urlsDatabase;

    private final UserUrlRepository userUrlRepository;


    @Autowired
    public UrlRepositoryImpl(@Qualifier("urlsDatabase") Path urlsDatabase, UserUrlRepository userUrlRepository) {
        this.urlsDatabase = urlsDatabase;
        this.userUrlRepository = userUrlRepository;
    }

    @Override
    public <S extends Url> S save(S url) {
        Long urlId = url.getId();
        if (!existsById(urlId)) {
            writeUrlToDatabase(url);

        } else {
            if (userUrlRepository.findAllUserIdsByUrlId(urlId).size() > 1) {
                Optional<Long> userUrlId = userUrlRepository.getIdByPair(new UserUrl(url.getOwner().getId(), urlId));
                userUrlRepository.deleteById(userUrlId.get());
                url.setId(getAvailableId());
                writeUrlToDatabase(url);

            } else {
                String line;
                String[] urlData;
                StringBuilder updatedFile = new StringBuilder();

                try (var reader = Files.newBufferedReader(urlsDatabase)) {
                    while ((line = reader.readLine()) != null) {
                        urlData = line.split(",");

                        if (urlData[0].equals(String.valueOf(urlId))) {
                            updatedFile.append(urlId);
                            updatedFile.append(",");
                            updatedFile.append(url.getPath());
                            updatedFile.append("\n");
                        } else {
                            updatedFile.append(line).append("\n");
                        }
                    }
                } catch (IOException e) {
                    throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
                }

                try (var writer = Files.newBufferedWriter(urlsDatabase)) {
                    writer.write(updatedFile.toString());
                } catch (IOException e) {
                    throw new DatabaseOperationException("Exception has while writing to the Url database");
                }
            }
        }

        return url;
    }

    private <S extends Url> void writeUrlToDatabase(S url) {
        try (var writer = Files.newBufferedWriter(urlsDatabase, StandardOpenOption.APPEND)) {
            writer.append(String.valueOf(url.getId()));
            writer.append(",");
            writer.append(url.getPath());
            writer.append("\n");

        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the Url database");
        }

        userUrlRepository.save(new UserUrl(userUrlRepository.getAvailableId(), url.getOwner().getId(), url.getId()));
    }

    @Override
    public <S extends Url> List<S> saveAll(List<S> urls) {
        for (Url url : urls) {
            save(url);
        }
        return urls;
    }

    @Override
    public Optional<Url> findById(Long id) {
        String line;
        String[] urlData;

        try (var reader = Files.newBufferedReader(urlsDatabase)) {
            while ((line = reader.readLine()) != null) {
                urlData = line.split(",");

                if (urlData[0].equals(String.valueOf(id))) {
                    return Optional.of(new Url(Long.parseLong(urlData[0]), urlData[1]));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        String line;
        String[] urlData;

        try (var reader = Files.newBufferedReader(urlsDatabase)) {
            while ((line = reader.readLine()) != null) {
                urlData = line.split(",");

                if (urlData[0].equals(String.valueOf(id))) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        return false;
    }

    @Override
    public List<Url> findAll() {
        String line;
        String[] urlData;
        List<Url> urls = new ArrayList<>();

        try (var reader = Files.newBufferedReader(urlsDatabase)) {
            while ((line = reader.readLine()) != null) {
                urlData = line.split(",");
                urls.add(new Url(Long.parseLong(urlData[0]), urlData[1]));
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        return urls;
    }

    @Override
    public long count() {
        long count = 0;

        try(var reader = Files.newBufferedReader(urlsDatabase)) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        return count;
    }

    @Override
    public void deleteById(Long id) {
        String line;
        String[] urlData;
        StringBuilder updatedFile = new StringBuilder();

        try (var reader = Files.newBufferedReader(urlsDatabase)) {
            while ((line = reader.readLine()) != null) {
                urlData = line.split(",");

                if (!urlData[0].equals(String.valueOf(id))) {
                    updatedFile.append(line);
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        try (var writer = Files.newBufferedWriter(urlsDatabase)) {
            writer.write(updatedFile.toString());
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the Url database");
        }

        userUrlRepository.deleteAllByUrlId(id);
    }

    @Override
    public void deleteAll() {
        try (var writer = Files.newBufferedWriter(urlsDatabase)) {
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while writing to the Url database");
        }

        userUrlRepository.deleteAll();
    }

    @Override
    public long getAvailableId() {
        String line;
        String[] urlData = null;

        try (var reader = Files.newBufferedReader(urlsDatabase)) {
            while ((line = reader.readLine()) != null) {
                urlData = line.split(",");
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        return urlData != null ? (Long.parseLong(urlData[0]) + 1) : 1;
    }

    @Override
    public Optional<Url> findByPath(String path) {
        String line;
        String[] urlData;

        try (var reader = Files.newBufferedReader(urlsDatabase)) {
            while ((line = reader.readLine()) != null) {
                urlData = line.split(",");

                if (urlData[1].equals(path)) {
                    return Optional.of(new Url(Long.parseLong(urlData[0]), urlData[1]));
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
        }

        return Optional.empty();
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        if (userUrlRepository.findAllUserIdsByUrlId(id).size() > 1) {
            Optional<Long> userUrlId = userUrlRepository.getIdByPair(new UserUrl(userId, id));
            userUrlRepository.deleteById(userUrlId.get());
        } else {
            String line;
            String[] urlData;
            StringBuilder updatedFile = new StringBuilder();

            try (var reader = Files.newBufferedReader(urlsDatabase)) {
                while ((line = reader.readLine()) != null) {
                    urlData = line.split(",");

                    if (!urlData[0].equals(String.valueOf(id))) {
                        updatedFile.append(line);
                    }
                }
            } catch (IOException e) {
                throw new DatabaseOperationException("Exception has occurred while reading from the Url database");
            }

            try (var writer = Files.newBufferedWriter(urlsDatabase)) {
                writer.write(updatedFile.toString());
            } catch (IOException e) {
                throw new DatabaseOperationException("Exception has occurred while writing to the Url database");
            }

            userUrlRepository.deleteAllByUrlId(id);
        }
    }
}
