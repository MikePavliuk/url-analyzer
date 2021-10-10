package com.mykhailopavliuk.service;

import com.mykhailopavliuk.exception.EntityNotFoundException;
import com.mykhailopavliuk.exception.NullEntityReferenceException;
import com.mykhailopavliuk.model.User;
import com.mykhailopavliuk.repository.UserRepository;
import com.mykhailopavliuk.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User expected;

    @BeforeEach
    public void setUp() {
        expected = new User();
        expected.setId(1L);
        expected.setEmail("green@mail.com");
        expected.setPassword("1111".toCharArray());
    }

    @AfterEach
    public void tearDown() {
        expected = null;
    }

    @Test
    public void testCorrectCreate() {
        when(userRepository.save(expected)).thenReturn(expected);
        User actual = userService.create(expected);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(expected);
    }

    @Test
    public void testExceptionCreate() {
        Exception exception = assertThrows(NullEntityReferenceException.class,
                () -> userService.create(null)
        );

        assertEquals("User cannot be 'null'", exception.getMessage());
        verify(userRepository, never()).save(new User());
    }

    @Test
    public void testCorrectReadById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        User actual = userService.readById(anyLong());

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testExceptionReadById() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.readById(100L)
        );

        assertEquals("User with id 100 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(100L);
    }

    @Test
    public void testCorrectUpdate() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(userRepository.save(expected)).thenReturn(expected);
        User actual = userService.update(expected);

        assertEquals(expected, actual);
        verify(userRepository, times(1)).save(expected);
    }

    @Test
    public void testExceptionUpdate() {
        Exception exception = assertThrows(NullEntityReferenceException.class, ()
                -> userService.update(null)
        );

        assertEquals("User cannot be 'null'", exception.getMessage());
        verify(userRepository, never()).save(new User());
    }

    @Test
    public void testDelete() {
        doNothing().when(userRepository).deleteById(anyLong());
        userService.deleteById(anyLong());

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetAll() {
        List<User> expected = List.of(new User(), new User(), new User());

        when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.getAll();

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCorrectReadByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(expected));
        User actual = userService.readByEmail(anyString());

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testExceptionReadByEmail() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.readByEmail("unknown@gmail.com")
        );

        assertEquals("User with email 'unknown@gmail.com' not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("unknown@gmail.com");
    }

    @Test
    public void testGetAvailableId() {
        when(userRepository.getAvailableId()).thenReturn(expected.getId()+1);

        assertEquals(expected.getId()+1, userService.getAvailableId());
        verify(userRepository, times(1)).getAvailableId();
    }

}
