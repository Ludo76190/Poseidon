package com.nnk.springboot.services;

import com.nnk.springboot.config.exception.AlreadyExistException;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of interface UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates User.
     * @param user the BidList to create
     */
    @Override
    public void createUser(User user) throws Exception {
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            logger.error("username "+ user.getUsername() +" already exist");
            throw new AlreadyExistException("Username " + user.getUsername() + " already exists.");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Success password encoded");
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("Success create user with encoded password");

    }

    /**
     * Updates a user
     * @param user the user to update
     * @param id id of the user to update
     */
    @Override
    public void updateUser(User user, Integer id) throws Exception {
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            logger.error("username "+ user.getUsername() +" already exist");
            throw new AlreadyExistException("Username " + user.getUsername() + " already exists.");
        }
        user.setId(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("Success update user" + user.getUsername());

    }

    /**
     * Get all user
     * @return all users
     */
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    /**
     * returns user from an id
     * @param id the user's id
     * @return the user
     */
    @Override
    public User getUserById(Integer id) {
        return userRepository.getOne(id);
    }

    /**
     * delete user from an id
     * @param id the user's id
     */
    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
        logger.info("Success delete user " + id);
    }

    /**
     * returns user from a username
     * @param username the user's username
     * @return the user
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
