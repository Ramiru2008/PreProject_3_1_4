package com.example.preproject_3_1_4.services;

import com.example.preproject_3_1_4.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void add(User user);

    void removeUserById(Long id);

    User getUserById(Long id);

    User findByUsername(String username);

    void edit(User user);
}
