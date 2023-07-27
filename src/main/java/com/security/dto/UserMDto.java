package com.security.dto;

import com.security.model.RoleM;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMDto {

    private Long user_id;

    private String username;

    private String password;

    private String email;

    private String number;
    private List<String> roles;

}
