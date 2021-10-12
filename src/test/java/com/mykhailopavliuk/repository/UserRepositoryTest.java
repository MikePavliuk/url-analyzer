package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.TestConfig;
import com.mykhailopavliuk.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles({ "test" })
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    public void testSaveNewUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail.com");
        user1.setPassword("password".toCharArray());

        User expected = userRepository.save(user1);
        User actual = userRepository.findById(1L).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveUpdatedUser() {
        User user1 = new User();
        user1.setId(2);
        user1.setEmail("oldUser@gmail.com");
        user1.setPassword("password".toCharArray());

        userRepository.save(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("newUser@gmail.com");
        user2.setPassword("newPassword".toCharArray());

        User updatedUser = userRepository.save(user2);

        assertEquals(updatedUser, userRepository.findByEmail("newUser@gmail.com").orElse(null));
    }

    @Test
    public void testSaveAllNewUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@gmail.com");
        user1.setPassword("password1".toCharArray());

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password2".toCharArray());

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<User> expected = userRepository.saveAll(users);

        assertEquals(expected, userRepository.findAll());
    }

    @Test
    public void testSaveAllWithUpdatedUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@gmail.com");
        user1.setPassword("password1".toCharArray());

        userRepository.save(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password2".toCharArray());

        userRepository.save(user2);

        User user3 = new User();
        user3.setId(3);
        user3.setEmail("user3@gmail.com");
        user3.setPassword("password3".toCharArray());

        User user4 = new User();
        user4.setId(1);
        user4.setEmail("user1updated@gmail.com");
        user4.setPassword("password1updated".toCharArray());

        List<User> users = new ArrayList<>();
        users.add(user3);
        users.add(user4);

        userRepository.saveAll(users);

        assertTrue(
                3 == userRepository.count() &&
                        user4.equals(userRepository.findByEmail("user1updated@gmail.com").orElse(null))
                );
    }

    @Test
    public void testFindByIdExistedUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail.com");
        user1.setPassword("password".toCharArray());

        User expected = userRepository.save(user1);
        User actual = userRepository.findById(1L).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdNotExistedUser() {
        assertNull(userRepository.findById(10L).orElse(null));
    }

    @Test
    public void testExistsByIdExistedUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@gmail.com");
        user1.setPassword("password1".toCharArray());
        userRepository.save(user1);

        assertTrue(userRepository.existsById(1L));
    }

    @Test
    public void testExistsByIdNotExistedUser() {
        assertFalse(userRepository.existsById(10L));
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password2".toCharArray());

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        userRepository.save(user1);
        userRepository.save(user2);

        assertEquals(expected, userRepository.findAll());
    }

    @Test
    public void testCount() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password2".toCharArray());

        userRepository.save(user1);
        userRepository.save(user2);

        assertEquals(2, userRepository.count());
    }

    @Test
    public void testDeleteByIdExistedUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());
        userRepository.save(user1);

        userRepository.deleteById(1L);

        assertEquals(0, userRepository.count());
    }

    @Test
    public void testDeleteByIdNotExistedUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());
        userRepository.save(user1);

        userRepository.deleteById(2L);

        assertEquals(1, userRepository.count());
    }

    @Test
    public void testDeleteAll() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password2".toCharArray());

        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.deleteAll();

        assertEquals(0, userRepository.count());
    }

    @Test
    public void testFindByEmailExistedUser() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());

        userRepository.save(user1);

        assertEquals(user1, userRepository.findByEmail("user@gmail1.com").orElse(null));
    }

    @Test
    public void testFindByEmailNotExistedUser() {
        assertNull(userRepository.findByEmail("user@gmail1.com").orElse(null));
    }

    @Test
    public void testGetAvailableId() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user@gmail1.com");
        user1.setPassword("password1".toCharArray());

        userRepository.save(user1);

        assertEquals(2, userRepository.getAvailableId());
    }
}
