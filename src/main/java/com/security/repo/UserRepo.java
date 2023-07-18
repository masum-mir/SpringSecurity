package com.security.repo;

import com.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

    @Autowired
    private JdbcTemplate jdb;

    public User findByUserName(String username) {

        String sql = "select * from users where username = ?";

        Object[] args = {username};

        return jdb.queryForObject(sql, args, new BeanPropertyRowMapper<User>(User.class));

    }
}
