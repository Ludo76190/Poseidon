package com.nnk.springboot.controllers;

import com.nnk.springboot.config.exception.AlreadyExistException;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserDetailsServiceImpl;
import com.nnk.springboot.services.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getUserListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getUserList() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setPassword("password");
        user.setFullname("User Test");
        user.setRole("USER");
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.getAllUser()).thenReturn(users);

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("users", users));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getUserListWithUSERAuthority() throws Exception {

        mockMvc.perform(get("/user/list"))
                .andExpect(status().is(403))
                .andExpect(forwardedUrl("/403"));

    }

    @Test
    public void getUserAddWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getUserAdd() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getUserUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/user/update/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getUserUpdateWithException() throws Exception {
        when(userService.getUserById(0)).thenThrow(new IllegalArgumentException("Invalid user Id:0"));
        mockMvc.perform(get("/user/update/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid user Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getUserUpdate() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setPassword("password");
        user.setFullname("User Test");
        user.setRole("USER");
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("user", user));
    }

    @Test
    public void getUserDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/user/delete/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getUserDelete() throws Exception {

        mockMvc.perform(get("/user/delete/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Delete successful"));
    }

    @Test
    public void postUserValidateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidateExistingUser() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@Test5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "Add successful"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidateEmpty() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "")
                        .param("fullname", "")
                        .param("role", "")
                        .param("password", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "username", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "fullname", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "role", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"))
        ;
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidatePasswordTooShort() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "U@t5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidatePasswordWithoutNumber() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidatePasswordWithoutSpecialChar() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "UserTest5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidatePasswordWithoutUppercase() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "user@test5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"));
    }

    @Test
    public void postUserUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/user/update/0")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserUpdate() throws Exception {

        mockMvc.perform(post("/user/update/0")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test5")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserUpdateEmpty() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setPassword("password");
        user.setFullname("User Test");
        user.setRole("USER");
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(post("/user/update/1")
                        .param("username", "")
                        .param("fullname", "")
                        .param("role", "")
                        .param("password", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "username", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "fullname", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "role", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"))
        ;
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserUpdatePasswordInvalid() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setPassword("password");
        user.setFullname("User Test");
        user.setRole("USER");
        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(post("/user/update/1")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("user", "password", "ValidPassword"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserValidateWithException() throws Exception {
        doThrow(new SQLException()).when(userService).createUser(any(User.class));
        mockMvc.perform(post("/user/validate")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "Issue during creating user, please retry later"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserUpdateWithException() throws Exception {
        doThrow(new Exception()).when(userService).updateUser(any(User.class), eq(0));
        mockMvc.perform(post("/user/update/0")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test5")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Issue during updating, please retry later"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserUpdateWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid user Id:0")).when(userService).updateUser(any(User.class), eq(0));
        mockMvc.perform(post("/user/update/0")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test5")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid user Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postUserUpdateWithAlreadyExistException() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setPassword("password");
        user.setFullname("User Test");
        user.setRole("USER");
        doThrow(new AlreadyExistException("You can't change the username to usertest, it already exists")).when(userService).updateUser(any(User.class), eq(1));
        when(userService.getUserById(1)).thenReturn(user);
        mockMvc.perform(post("/user/update/1")
                        .param("username", "usertest")
                        .param("fullname", "User Test")
                        .param("role", "ADMIN")
                        .param("password", "User@test5")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "You can't change the username to usertest, it already exists"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getUserDeleteWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid user Id:0")).when(userService).deleteUser(eq(0));
        mockMvc.perform(get("/user/delete/0")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid user Id:0"));
    }


}