package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.TestConfig;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.model.UserUrl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles({ "test" })
public class UserUrlRepositoryTest {

    @Autowired
    private UserUrlRepository userUrlRepository;

    private UserUrl userUrl1, userUrl2;

    @BeforeEach
    void clearDB() {
        userUrlRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        userUrl1 = new UserUrl();
        userUrl1.setId(10);
        userUrl1.setUserId(1);
        userUrl1.setUrlId(2);

        userUrl2 = new UserUrl();
        userUrl2.setId(11);
        userUrl2.setUserId(2);
        userUrl2.setUrlId(2);
    }

    @AfterEach
    public void tearDown() {
        userUrl1 = null;
        userUrl2 = null;
    }

    @Test
    public void testSaveNewUserUrl() {
        UserUrl expected = userUrlRepository.save(userUrl1);
        UserUrl actual = userUrlRepository.findById(10L).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveAll() {
        List<UserUrl> userUrlList = new ArrayList<>();
        userUrlList.add(userUrl1);
        userUrlList.add(userUrl2);

        List<UserUrl> expected = userUrlRepository.saveAll(userUrlList);

        assertEquals(expected, userUrlRepository.findAll());
    }

    @Test
    public void testFindByIdExistedUserUrl() {
        UserUrl expected = userUrlRepository.save(userUrl1);
        UserUrl actual = userUrlRepository.findById(10L).orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdNotExistedUserUrl() {
        assertNull(userUrlRepository.findById(10L).orElse(null));
    }

    @Test
    public void testExistsByIdExistedUserUrl() {
        userUrlRepository.save(userUrl1);

        assertTrue(userUrlRepository.existsById(10L));
    }

    @Test
    public void testExistsByIdNotExistedUserUrl() {
        assertFalse(userUrlRepository.existsById(10L));
    }

    @Test
    public void testFindAll() {
        List<UserUrl> userUrlList = new ArrayList<>();
        userUrlList.add(userUrl1);
        userUrlList.add(userUrl2);

        List<UserUrl> expected = userUrlRepository.saveAll(userUrlList);

        assertEquals(expected, userUrlRepository.findAll());
    }

    @Test
    public void testCount() {
        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);

        assertEquals(2, userUrlRepository.count());
    }

    @Test
    public void testDeleteByIdExistedUser() {
        userUrlRepository.save(userUrl1);

        userUrlRepository.deleteById(10L);

        assertEquals(0, userUrlRepository.count());
    }

    @Test
    public void testDeleteByIdNotExistedUser() {
        userUrlRepository.save(userUrl1);

        userUrlRepository.deleteById(20L);

        assertEquals(1, userUrlRepository.count());
    }

    @Test
    public void testDeleteAll() {
        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);

        userUrlRepository.deleteAll();

        assertEquals(0, userUrlRepository.count());
    }

    @Test
    public void testGetAvailableId() {
        userUrlRepository.save(userUrl1);

        assertEquals(11, userUrlRepository.getAvailableId());
    }

    @Test
    public void testFindAllFirstEntityIdsBySecondEntityId() {
        userUrl1.setUrlId(1);

        UserUrl userUrl3 = new UserUrl();
        userUrl3.setId(15);
        userUrl3.setUserId(3);
        userUrl3.setUrlId(2);

        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);
        userUrlRepository.save(userUrl3);

        List<Long> expected = new ArrayList<>();
        expected.add(2L);
        expected.add(3L);

        List<Long> actual = userUrlRepository.findAllFirstEntityIdsBySecondEntityId(2L);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllSecondEntityIdsByFirstEntityId() {
        UserUrl userUrl3 = new UserUrl();
        userUrl3.setId(15);
        userUrl3.setUserId(1);
        userUrl3.setUrlId(3);

        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);
        userUrlRepository.save(userUrl3);

        List<Long> expected = new ArrayList<>();
        expected.add(2L);
        expected.add(3L);

        List<Long> actual = userUrlRepository.findAllSecondEntityIdsByFirstEntityId(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteAllByFirstEntityId() {
        userUrl2.setUserId(1);
        userUrl2.setUrlId(3);

        UserUrl userUrl3 = new UserUrl();
        userUrl3.setId(12);
        userUrl3.setUserId(2);
        userUrl3.setUrlId(3);

        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);
        userUrlRepository.save(userUrl3);

        userUrlRepository.deleteAllByFirstEntityId(1L);

        assertEquals(1, userUrlRepository.count());
    }

    @Test
    public void testDeleteAllBySecondEntityId() {
        userUrl2.setUrlId(3);

        UserUrl userUrl3 = new UserUrl();
        userUrl3.setId(12);
        userUrl3.setUserId(2);
        userUrl3.setUrlId(3);

        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);
        userUrlRepository.save(userUrl3);

        userUrlRepository.deleteAllBySecondEntityId(2L);

        assertEquals(1, userUrlRepository.count());
    }

    @Test
    public void testGetIdByPairExisted() {
        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);

        assertEquals(11L, userUrlRepository.getIdByPair(new UserUrl(2, 2)).orElse(0L));
    }
    @Test
    public void testGetIdByPairNotExisted() {
        userUrlRepository.save(userUrl1);
        userUrlRepository.save(userUrl2);

        assertNull(userUrlRepository.getIdByPair(new UserUrl(3, 2)).orElse(null));
    }

}
