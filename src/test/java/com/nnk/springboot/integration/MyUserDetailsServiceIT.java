package com.nnk.springboot.integration;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MyUserDetailsServiceIT {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void loadUserByUsernameNotFoundUser()
    {
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
                    userDetailsService.loadUserByUsername("notAUser");
                }
        );

        assertThat(exception.getMessage()).contains("Username notAUser not found");
    }

    @Test
    public void loadUserByUsernameExistingUser()
    {
        User user = new User();
        user.setUsername("username");
        user.setPassword("myPassword");
        user.setRole("USER");
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }
}
