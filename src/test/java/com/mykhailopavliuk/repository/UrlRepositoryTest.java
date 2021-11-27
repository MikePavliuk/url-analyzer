package com.mykhailopavliuk.repository;

import com.mykhailopavliuk.TestConfig;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.model.UserUrl;
import com.mykhailopavliuk.repository.impl.UrlRepositoryImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles({"test"})
public class UrlRepositoryTest {

    @MockBean
    private UserUrlRepository userUrlRepository;

    @Autowired
    private UrlRepositoryImpl urlRepository;

    private User user;
    private Url url1, url2;

    @BeforeEach
    public void setUp() {
        urlRepository.deleteAll();

        user = new User();
        user.setId(1L);

        url1 = new Url();
        url1.setId(1);
        url1.setOwner(user);
        url1.setPath("https://example1.com");

        url2 = new Url();
        url2.setId(2);
        url2.setOwner(user);
        url2.setPath("https://example2.com");
    }

    @AfterEach
    public void tearDown() {
        user = null;
        url1 = null;
        url2 = null;

        urlRepository.deleteAll();
    }

    @Test
    public void testSaveNewUrl() {
        when(userUrlRepository.save(any(UserUrl.class))).thenReturn(null);
        when(userUrlRepository.getAvailableId()).thenReturn(1L);

        Url expected = urlRepository.save(url1);
        Url actual = urlRepository.findById(1L).orElse(null);

        assertEquals(expected, actual);

        verify(userUrlRepository, times(1)).save(new UserUrl(1L, 1L, 1L));
        verify(userUrlRepository, times(1)).getAvailableId();
    }

    @Test
    public void testSaveUpdatedUrlWhileSomeoneElseUsesOldOne() {
        when(userUrlRepository.findAllUserIdsByUrlId(anyLong())).thenReturn(new ArrayList<>() {{
            add(1L);
            add(2L);
        }});
        when(userUrlRepository.getIdByPair(any(UserUrl.class))).thenReturn(Optional.of(1L));
        when(userUrlRepository.save(any(UserUrl.class))).thenReturn(null);
        when(userUrlRepository.getAvailableId()).thenReturn(2L);
        doNothing().when(userUrlRepository).deleteById(anyLong());

        urlRepository.save(url1);

        url1.setPath("https://newPath.com");

        Url expected = urlRepository.save(url1);

        assertTrue(
                urlRepository.count() == 2 &&
                        expected.equals(urlRepository.findById(2L).orElse(null))
        );

        verify(userUrlRepository, times(2)).findAllUserIdsByUrlId(1L);
        verify(userUrlRepository, times(1)).getIdByPair(new UserUrl(1L, 1L));
        verify(userUrlRepository, times(1)).deleteById(1L);
        verify(userUrlRepository, times(1)).save(new UserUrl(2L, 1L, 2L));
    }

    @Test
    public void testSaveUpdatedUrlWhileNoOneUsesOldOne() {
        when(userUrlRepository.findAllUserIdsByUrlId(anyLong())).thenReturn(new ArrayList<>() {{
            add(1L);
        }});

        urlRepository.save(url1);

        url1.setPath("https://newPath.com");

        Url expected = urlRepository.save(url1);

        assertTrue(
                urlRepository.count() == 1 &&
                        expected.equals(urlRepository.findById(1L).orElse(null))
        );

        verify(userUrlRepository, times(1)).save(any(UserUrl.class));
        verify(userUrlRepository, times(1)).findAllUserIdsByUrlId(1L);
    }

    @Test
    public void testSaveAllUrls() {
        List<Url> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);

        List<Url> expected = urlRepository.saveAll(urls);

        assertEquals(expected, urlRepository.findAll());

        verify(userUrlRepository, times(2)).save(any(UserUrl.class));
    }

    @Test
    public void testFindByIdExistedUrl() {
        urlRepository.save(url1);

        assertEquals(url1, urlRepository.findById(1L).orElse(null));
    }

    @Test
    public void testFindByIdNotExistedUrl() {
        assertNull(urlRepository.findById(1L).orElse(null));
    }

    @Test
    public void testExistsByIdExistedUser() {
        urlRepository.save(url1);

        assertTrue(urlRepository.existsById(1L));
    }

    @Test
    public void testExistsByIdNotExistedUrl() {
        assertFalse(urlRepository.existsById(1L));
    }

    @Test
    public void testFindAll() {
        List<Url> expected = new ArrayList<>();
        expected.add(url1);
        expected.add(url2);

        urlRepository.save(url1);
        urlRepository.save(url2);

        assertEquals(expected, urlRepository.findAll());
    }

    @Test
    public void testCount() {
        urlRepository.save(url1);
        urlRepository.save(url2);

        assertEquals(2, urlRepository.count());
    }

    @Test
    public void testDeleteByIdExistedUrl() {
        urlRepository.save(url1);

        urlRepository.deleteById(1L);

        assertEquals(0, urlRepository.count());

        verify(userUrlRepository, times(1)).deleteAllByUrlId(1L);
    }

    @Test
    public void testDeleteByIdNotExistedUrl() {
        urlRepository.save(url1);

        urlRepository.deleteById(2L);

        assertEquals(1, urlRepository.count());
    }

    @Test
    public void testDeleteAll() {
        urlRepository.save(url1);
        urlRepository.save(url2);

        urlRepository.deleteAll();

        assertEquals(0, urlRepository.count());
    }

    @Test
    public void testFindByPathExistedUrl() {
        urlRepository.save(url1);

        assertEquals(url1, urlRepository.findByPath(url1.getPath()).orElse(null));
    }

    @Test
    public void testFindByPathNotExistedUrl() {
        assertNull(urlRepository.findByPath(url1.getPath()).orElse(null));
    }

    @Test
    public void testGetAvailableId() {
        urlRepository.save(url1);

        assertEquals(2, urlRepository.getAvailableId());
    }

    @Test
    public void testDeleteByIdAndUserWhileSomeoneElseUsesUrl() {
        when(userUrlRepository.findAllUserIdsByUrlId(anyLong())).thenReturn(new ArrayList<>() {{
            add(1L);
            add(2L);
        }});
        when(userUrlRepository.getIdByPair(any(UserUrl.class))).thenReturn(Optional.of(1L));
        doNothing().when(userUrlRepository).deleteById(anyLong());

        urlRepository.save(url1);
        urlRepository.deleteByIdAndUserId(1L, 1L);

        assertEquals(1, urlRepository.count());

        verify(userUrlRepository, times(1)).findAllUserIdsByUrlId(1L);
        verify(userUrlRepository, times(1)).getIdByPair(new UserUrl(1L, 1L));
        verify(userUrlRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteByIdAndUserWhileNoOneElseUsesUrl() {
        when(userUrlRepository.findAllUserIdsByUrlId(anyLong())).thenReturn(new ArrayList<>() {{
            add(1L);
        }});
        doNothing().when(userUrlRepository).deleteAllByUrlId(anyLong());

        urlRepository.save(url1);
        urlRepository.deleteByIdAndUserId(1L, 1L);

        assertEquals(0, urlRepository.count());

        verify(userUrlRepository, times(1)).findAllUserIdsByUrlId(1L);
        verify(userUrlRepository, times(1)).deleteAllByUrlId(1L);
    }

}
