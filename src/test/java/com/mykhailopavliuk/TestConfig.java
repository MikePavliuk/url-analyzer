package com.mykhailopavliuk;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.nio.file.Paths;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public File testDatabaseFile() {
        return Paths.get("src/test/resources/database.csv").toFile();
    }
}
