package com.security.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Data
@Entity
public class UserM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String number;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "userm_rolesm",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleM> roles = new HashSet<>();


}
