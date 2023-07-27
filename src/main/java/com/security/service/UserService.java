package com.security.service;

import com.security.model.UserM;
import com.security.repo.UserMRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMRepo userRepo;

    public UserM findByUserName(String username) {
        return  userRepo.findByUsername(username);
    }

}
