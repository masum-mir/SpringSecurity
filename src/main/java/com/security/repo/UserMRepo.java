package com.security.repo;

import com.security.model.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMRepo extends JpaRepository<UserM, Long> {

    UserM findByUsername(String username);

}
