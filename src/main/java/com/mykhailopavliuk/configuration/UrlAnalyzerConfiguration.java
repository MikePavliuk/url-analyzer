package com.mykhailopavliuk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class UrlAnalyzerConfiguration {

    @Bean
    public File databaseFile() {
        return Paths.get("src/main/resources/database.csv").toFile();
    }
}
