package com.example.userservice.service.impl;


import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Optional<User> findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public boolean Login(String account, String password) {
        Optional<User> user = userRepository.findByAccount(account);
        return user.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent();
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByUserName(name);
    }

    @Override
    public boolean Register(String account, String password) {
        if (account.isEmpty() || password.isEmpty()) return false;
        if (findByAccount(account).isEmpty()) {
                User user = new User();
                user.setAccount(account);
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
                user.setUserName("用户" + account);
                user.setGender("");
                userRepository.save(user);
                return true;
        }
        return false;
    }

    public List<User> FindAll() {
        return userRepository.findAll();
    }

    public void deleteByUserId(String userId) {userRepository.deleteById(userId);}
}
