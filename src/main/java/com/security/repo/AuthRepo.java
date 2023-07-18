package com.security.repo;

import com.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepo {

    @Autowired
    private JdbcTemplate jdb;

    @Autowired
    private NamedParameterJdbcTemplate ndb;

    public User findByUserName(String username) {

        String sql = "select * from users where username = ?";

        Object[] args = {username};

//        Map<String, Object> params = new HashMap<>();
//        params.put("username", username);

        try {
            return jdb.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            return null;
        }


    }
}
