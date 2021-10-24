package com.mykhailopavliuk.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Profile({ "production" })
public class UrlAnalyzerConfiguration {

    @Bean
    @Qualifier("usersDatabase")
    public Path getUsersDatabaseFile() throws IOException {
        Path userDatabase = Paths.get("src/main/resources/database/users.csv");

        if (!Files.exists(userDatabase)) {
            Files.createFile(userDatabase);
        }

        return userDatabase;
    }

    @Bean
    @Qualifier("urlsDatabase")
    public Path getUrlsDatabaseFile() throws IOException {
        Path urlsDatabase = Paths.get("src/main/resources/database/urls.csv");

        if (!Files.exists(urlsDatabase)) {
            Files.createFile(urlsDatabase);
        }

        return urlsDatabase;
    }

    @Bean
    @Qualifier("user_urlDatabase")
    public Path getUserUrlDatabaseFile() throws IOException {
        Path user_urlDatabase = Paths.get("src/main/resources/database/user-url.csv");

        if (!Files.exists(user_urlDatabase)) {
            Files.createFile(user_urlDatabase);
        }

        return user_urlDatabase;
    }
}
