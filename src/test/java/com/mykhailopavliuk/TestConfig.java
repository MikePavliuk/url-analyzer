package com.mykhailopavliuk;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.file.Paths;

@TestConfiguration
@Profile({ "test" })
public class TestConfig {

    @Bean
    @Qualifier("usersDatabase")
    public File getUsersDatabaseFile() {
        return Paths.get("src/test/resources/database/users.csv").toFile();
    }

    @Bean
    @Qualifier("urlsDatabase")
    public File getUrlsDatabaseFile() {
        return Paths.get("src/test/resources/database/urls.csv").toFile();
    }

    @Bean
    @Qualifier("user_urlDatabase")
    public File getUserUrlDatabaseFile() {
        return Paths.get("src/test/resources/database/user-url.csv").toFile();
    }
}
