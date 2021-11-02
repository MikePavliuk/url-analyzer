package com.mykhailopavliuk.configuration.spring;

import com.mykhailopavliuk.configuration.application.AdminProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    @Bean
    @Qualifier("settings")
    public Path getSettingsFile() throws IOException {
        Path settings = Paths.get("src/main/resources/settings/settings.xml");

        if (!Files.exists(settings)) {
            Files.createFile(settings);
        }

        return settings;
    }

    @Bean
    @ConfigurationProperties(prefix = "admin")
    public AdminProperties getAdminProperties() {
        return new AdminProperties();
    }
}
