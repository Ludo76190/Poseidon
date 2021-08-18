package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;

import java.util.List;

public interface UserService {
    void createUser(User user) throws Exception;
    void updateUser(User user, Integer id) throws Exception;
    List<User> getAllUser();
    User getUserById(Integer id);
    void deleteUser(Integer id);
    User getUserByUsername(String username);
}
