package com.security.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class User {

    private Long user_id;
    private String username;
    private String password;
    private String email;
    private String number;
    private String role;

}
