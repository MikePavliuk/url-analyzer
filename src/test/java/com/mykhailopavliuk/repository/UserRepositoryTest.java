package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.TestConfig;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.model.UserUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles({ "test" })
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserUrlRepository userUrlRepository;

    @MockBean
    private UrlRepository urlRepository;

    private User user1, user2;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@gmail.com");
        user1.setPassword("password1".toCharArray());

        user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password2".toCharArray());
    }

    @AfterEach
    public void tearDown() {
        user1 = null;
        user2 = null;

        userRepository.deleteAll();
    }

    @Test
    public void testSaveNewUser() {
        User expected = userRepository.save(user1);
        User actual = userRepository.findById(1L).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveUpdatedUser() {
        userRepository.save(user1);

        user1.setEmail("newUser@gmail.com");
        user1.setPassword("newPassword".toCharArray());

        User updatedUser = userRepository.save(user1);

        assertTrue(
                userRepository.count() == 1 &&
                        updatedUser.equals(userRepository.findByEmail("newUser@gmail.com").orElse(null))
        );
    }

    @Test
    public void testSaveAllNewUsers() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<User> expected = userRepository.saveAll(users);

        assertEquals(expected, userRepository.findAll());
    }

    @Test
    public void testSaveAllWithUpdatedUsers() {
        userRepository.save(user1);
        userRepository.save(user2);

        user1.setEmail("user1updated@gmail.com");
        user1.setPassword("password1updated".toCharArray());

        User user3 = new User();
        user3.setId(3);
        user3.setEmail("user3@gmail.com");
        user3.setPassword("password3".toCharArray());

        List<User> users = new ArrayList<>();
        users.add(user3);
        users.add(user1);

        userRepository.saveAll(users);

        assertTrue(
                3 == userRepository.count() &&
                        user1.equals(userRepository.findByEmail("user1updated@gmail.com").orElse(null))
                );
    }

    @Test
    public void testFindByIdExistedUser() {
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
        userRepository.save(user1);

        assertTrue(userRepository.existsById(1L));
    }

    @Test
    public void testExistsByIdNotExistedUser() {
        assertFalse(userRepository.existsById(10L));
    }

    @Test
    public void testFindAll() {
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        userRepository.save(user1);
        userRepository.save(user2);

        assertEquals(expected, userRepository.findAll());
    }

    @Test
    public void testCount() {
        userRepository.save(user1);
        userRepository.save(user2);

        assertEquals(2, userRepository.count());
    }

    @Test
    public void testDeleteByIdExistedUser() {
        userRepository.save(user1);

        userRepository.deleteById(1L);

        assertEquals(0, userRepository.count());

        verify(userUrlRepository, times(1)).findAllUrlIdsByUserId(1L);
        verify(urlRepository, times(0)).findById(anyLong());
    }

    @Test
    public void testDeleteByIdNotExistedUser() {
        userRepository.save(user1);

        userRepository.deleteById(2L);

        assertEquals(1, userRepository.count());

        verify(userUrlRepository, times(0)).findAllUrlIdsByUserId(10L);
        verify(urlRepository, times(0)).findById(anyLong());
    }

    @Test
    public void testDeleteAll() {
        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.deleteAll();

        assertEquals(0, userRepository.count());

        verify(urlRepository, times(2)).deleteAll();
    }

    @Test
    public void testFindByEmailExistedUser() {
        userRepository.save(user1);

        assertEquals(user1, userRepository.findByEmail("user1@gmail.com").orElse(null));
    }

    @Test
    public void testFindByEmailNotExistedUser() {
        assertNull(userRepository.findByEmail("user@gmail1.com").orElse(null));
    }

    @Test
    public void testGetAvailableId() {
        userRepository.save(user1);

        assertEquals(2, userRepository.getAvailableId());
    }
}
