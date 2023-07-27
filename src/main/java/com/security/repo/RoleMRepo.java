package com.security.repo;

import com.security.model.ERole;
import com.security.model.RoleM;
import com.security.model.UserM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleMRepo extends JpaRepository<RoleM, Long> {

    Optional<RoleM> findByName(ERole name);

}
