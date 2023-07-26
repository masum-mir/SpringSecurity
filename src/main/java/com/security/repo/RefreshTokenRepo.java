package com.security.repo;

import com.security.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.*;

@Repository
public class RefreshTokenRepo {

    @Autowired
    private JdbcTemplate jdb;

    @Autowired
    private NamedParameterJdbcTemplate ndb;

    public Map<String, Object> findByToken(String token) {

        String sql = "SELECT rt.token, rt.expiry_date ,u.username  FROM refreshtoken as rt\n" +
                "left join users as u ON  rt.user_id = u.user_id \n" +
                "where token = '"+token+"'";

        Object[] args = {token};

        return jdb.queryForMap(sql);

    }

//    public boolean save(Map<String, Object> params) {
//        String sql = "insert into refreshtoken(id, expiry_date, token, user_id) values(:id, :expiry_date, :token, :userId)";
//
//        return ndb.update(sql, params) == 1;
//    }

    public RefreshToken save(RefreshToken token) {
        String sql = "insert into refreshtoken(id, expiry_date, token, user_id) values(?, ?, ?, ?)";

        jdb.update(sql, token.getId(), token.getExpiryDate(), token.getToken(),token.getUser_id());

        return token;
    }

}
