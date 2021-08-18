package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static User user;

    @BeforeEach
    public void initTest() {
        user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("passwordtest");
        user.setFullname("User Test");
        user.setRole("USER");
    }

    @Test
    void createUserTest() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);

    }

    @Test
    void updateUserTest() throws Exception {
        when(userRepository.getOne(1)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.updateUser(user, 1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUserTest() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);
        List<User> expectedBibList = userService.getAllUser();
        assertThat(expectedBibList).isEqualTo(users);
        verify(userRepository).findAll();
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.getOne(1)).thenReturn(user);
        User bibListTest = userService.getUserById(1);
        assertThat(bibListTest).isEqualTo(user);
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void getUserByUserNameTest() {
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);
        User userTest = userService.getUserByUsername(user.getUsername());
        assertThat(userTest).isEqualTo(user);
    }
}
