package com.mykhailopavliuk.configuration.spring;

import com.mykhailopavliuk.configuration.application.AdminProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Path;

@Configuration
@Profile({"production"})
public class UrlAnalyzerConfiguration {

    private final ResourceLoader resourceLoader;

    @Autowired
    public UrlAnalyzerConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @Qualifier("usersDatabase")
    public Path getUsersDatabaseFile() throws IOException {
        return resourceLoader.getResource("classpath:database/users.csv").getFile().toPath();
    }

    @Bean
    @Qualifier("urlsDatabase")
    public Path getUrlsDatabaseFile() throws IOException {
        return resourceLoader.getResource("classpath:database/urls.csv").getFile().toPath();
    }

    @Bean
    @Qualifier("user_urlDatabase")
    public Path getUserUrlDatabaseFile() throws IOException {
        return resourceLoader.getResource("classpath:database/user-url.csv").getFile().toPath();
    }

    @Bean
    @Qualifier("defaultSettings")
    public Path getDefaultSettingsFile() throws IOException {
        return resourceLoader.getResource("classpath:settings/settings.xml").getFile().toPath();
    }

    @Bean
    @Qualifier("settingsSchema")
    public Path getSettingsSchemaFile() throws IOException {
        return resourceLoader.getResource("classpath:settings/settings.xsd").getFile().toPath();
    }

    @Bean
    @Qualifier("settings")
    public Path getSettingsFile() {
        return Path.of("").toAbsolutePath().resolve("settings.xml");
    }

    @Bean
    @ConfigurationProperties(prefix = "admin")
    public AdminProperties getAdminProperties() {
        return new AdminProperties();
    }
}
