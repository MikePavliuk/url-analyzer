package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.TestConfig;
import com.mykhailopavliuk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;



    @Test
    public void testSaveUser() {
        User testUser = new User();
        testUser.setId(10);
        testUser.setEmail("someEmail");
        testUser.setPassword("password".toCharArray());
        testUser.setUrls(Collections.emptyList());
        userRepository.save(testUser);
        assertTrue(userRepository.findById(10L).isPresent());
    }


    @Test
    public void testNonExistingUser() {
        User testUser = new User();
        testUser.setId(1);
        testUser.setEmail("email");
        testUser.setPassword("password".toCharArray());
        testUser.setUrls(Collections.emptyList());
        userRepository.save(testUser);
        assertFalse(userRepository.findByEmail("").isPresent());
    }
}
