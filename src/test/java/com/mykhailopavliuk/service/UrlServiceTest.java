package com.mykhailopavliuk.service;

import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.exception.NullEntityReferenceException;
import com.mykhailopavliuk.model.Url;
import com.mykhailopavliuk.repository.UrlRepository;
import com.mykhailopavliuk.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    private Url expected;

    @BeforeEach
    public void setUp() {
        expected = new Url();
        expected.setId(1L);
        expected.setPath("https://example.com");
    }

    @AfterEach
    public void tearDown() {
        expected = null;
    }

    @Test
    public void testCorrectCreate() {
        when(urlRepository.save(expected)).thenReturn(expected);
        Url actual = urlService.create(expected);

        assertEquals(expected, actual);
        verify(urlRepository, times(1)).save(expected);
    }

    @Test
    public void testExceptionCreate() {
        Exception exception = assertThrows(NullEntityReferenceException.class,
                () -> urlService.create(null)
        );

        assertEquals("Url cannot be 'null'", exception.getMessage());
        verify(urlRepository, never()).save(new Url());
    }

    @Test
    public void testCorrectReadById() {
        when(urlRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        Url actual = urlService.readById(anyLong());

        assertEquals(expected, actual);
        verify(urlRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testExceptionReadById() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> urlService.readById(100L)
        );

        assertEquals("Url with id 100 not found", exception.getMessage());
        verify(urlRepository, times(1)).findById(100L);
    }

    @Test
    public void testCorrectUpdate() {
        when(urlRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(urlRepository.save(expected)).thenReturn(expected);
        Url actual = urlService.update(expected);

        assertEquals(expected, actual);
        verify(urlRepository, times(1)).save(expected);
    }

    @Test
    public void testExceptionUpdate() {
        Exception exception = assertThrows(NullEntityReferenceException.class, ()
                -> urlService.update(null)
        );

        assertEquals("Url cannot be 'null'", exception.getMessage());
        verify(urlRepository, never()).save(new Url());
    }

    @Test
    public void testDelete() {
        doNothing().when(urlRepository).deleteById(anyLong());
        urlService.deleteById(anyLong());

        verify(urlRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetAll() {
        List<Url> expected = List.of(new Url(), new Url(), new Url());

        when(urlRepository.findAll()).thenReturn(expected);
        List<Url> actual = urlService.getAll();

        assertEquals(expected, actual);
        verify(urlRepository, times(1)).findAll();
    }

    @Test
    public void testCorrectReadByPath() {
        when(urlRepository.findByPath(anyString())).thenReturn(Optional.of(expected));
        Url actual = urlService.readByPath(anyString());

        assertEquals(expected, actual);
        verify(urlRepository, times(1)).findByPath(anyString());
    }

    @Test
    public void testExceptionReadByPath() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> urlService.readByPath("https://unknown.com")
        );

        assertEquals("Url with path 'https://unknown.com' not found", exception.getMessage());
        verify(urlRepository, times(1)).findByPath("https://unknown.com");
    }

    @Test
    public void testGetAvailableId() {
        when(urlRepository.getAvailableId()).thenReturn(expected.getId() + 1);

        assertEquals(expected.getId() + 1, urlService.getAvailableId());
        verify(urlRepository, times(1)).getAvailableId();
    }

    @Test
    public void testDeleteByIdAndUserId() {
        doNothing().when(urlRepository).deleteByIdAndUserId(anyLong(), anyLong());
        urlService.deleteByIdAndUserId(1L, 1L);

        verify(urlRepository, times(1)).deleteByIdAndUserId(1L, 1L);
    }


}
