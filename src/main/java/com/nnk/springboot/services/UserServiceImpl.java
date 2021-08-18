package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) throws Exception {
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            logger.error("username "+ user.getUsername() +" already exist");
            throw new Exception("Username " + user.getUsername() +" already exists.");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Success password encoded");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("Success create user with encoded password");

    }

    @Override
    public void updateUser(User user, Integer id) throws Exception {
        user.setId(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("Success update user" + user.getUsername());

    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
        logger.info("Success delete user " + id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
