package com.security.service;

import com.security.model.User;
import com.security.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User findByUserName(String username) {
        return  userRepo.findByUserName(username);
    }

}
