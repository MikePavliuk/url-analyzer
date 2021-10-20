package com.mykhailopavliuk.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.file.Paths;

@Configuration
@Profile({ "production" })
public class UrlAnalyzerConfiguration {

    @Bean
    @Qualifier("usersDatabase")
    public File getUsersDatabaseFile() {
        return Paths.get("src/main/resources/database/users.csv").toFile();
    }

    @Bean
    @Qualifier("urlsDatabase")
    public File getUrlsDatabaseFile() {
        return Paths.get("src/main/resources/database/urls.csv").toFile();
    }

    @Bean
    @Qualifier("user_urlDatabase")
    public File getUserUrlDatabaseFile() {
        return Paths.get("src/main/resources/database/user-url.csv").toFile();
    }
}
