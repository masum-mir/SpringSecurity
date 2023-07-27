package com.security.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthRepo {

    @Autowired
    private JdbcTemplate jdb;

    @Autowired
    private NamedParameterJdbcTemplate ndb;

    public Map<String, Object> findByUserName(String username) {

        String sql = "select * from userm where username = :username";

//        Object[] args = {username};

//        try {
//            return jdb.queryForObject(sql, args, new BeanPropertyRowMapper<>(User.class));
//        } catch (Exception e) {
//            return null;
//        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        return ndb.queryForMap(sql, params);
    }
}
