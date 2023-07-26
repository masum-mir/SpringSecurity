package com.security.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshToken {

    private long id;

    private long user_id;

    private String token;

    private Instant expiryDate;

}
