package com.mykhailopavliuk;

import com.mykhailopavliuk.configuration.application.AdminProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@TestConfiguration
@Profile({ "test" })
public class TestConfig {

    @Bean
    @Qualifier("usersDatabase")
    public Path getUsersDatabaseFile() throws IOException {
        Path userDatabase = Paths.get("src/test/resources/database/users.csv");

        if (!Files.exists(userDatabase)) {
            Files.createFile(userDatabase);
        }

        return userDatabase;
    }

    @Bean
    @Qualifier("urlsDatabase")
    public Path getUrlsDatabaseFile() throws IOException {
        Path urlsDatabase = Paths.get("src/test/resources/database/urls.csv");

        if (!Files.exists(urlsDatabase)) {
            Files.createFile(urlsDatabase);
        }

        return urlsDatabase;
    }

    @Bean
    @Qualifier("user_urlDatabase")
    public Path getUserUrlDatabaseFile() throws IOException {
        Path user_urlDatabase = Paths.get("src/test/resources/database/user-url.csv");

        if (!Files.exists(user_urlDatabase)) {
            Files.createFile(user_urlDatabase);
        }

        return user_urlDatabase;
    }

    @Bean
    @Qualifier("defaultSettings")
    public Path getDefaultSettingsFile() {
        return Paths.get("src/test/resources/settings/settings.xml");
    }

    @Bean
    @Qualifier("settingsSchema")
    public Path getSettingsSchemaFile() {
        return Paths.get("src/test/resources/settings/settings.xsd");
    }

    @Bean
    @Qualifier("settings")
    public Path getSettingsFile() {
        return Paths.get("src/test/settings.xml");
    }


    @Bean
    @ConfigurationProperties(prefix = "admintest")
    public AdminProperties getAdminProperties() {
        return new AdminProperties();
    }
}
