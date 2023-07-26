package com.security.service;

import com.security.exception.TokenRefreshException;
import com.security.model.RefreshToken;
import com.security.repo.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo tokenRepo;

    public Map<String, Object> findByToken(String token) {
        return tokenRepo.findByToken(token);
    }

//    public boolean createRefreshToken(long userId) {
//
//        long id = Calendar.getInstance().getTimeInMillis();
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", id);
//        params.put("expiry_date", new Date());
////        params.put("expiry_date", params.put("expiry_date",120000););
//        params.put("token", UUID.randomUUID().toString());
//        params.put("userId", userId);
//
//        return tokenRepo.save(params);
//    }

    public RefreshToken createRefreshToken(long userId) {

        RefreshToken refreshToken = new RefreshToken();

        long id = Calendar.getInstance().getTimeInMillis();

        refreshToken.setId(id);
        refreshToken.setUser_id(userId);
        refreshToken.setExpiryDate(Instant.now().plusMillis(120000));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = tokenRepo.save(refreshToken);
        return refreshToken;
    }

    public boolean verifyRefreshToken(Map<String, Object> token) throws TokenRefreshException {

        String expiryDateString = token.get("expiry_date").toString();


        System.out.println("Date " + expiryDateString);

        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
        System.out.println("Date: " + now);

        System.out.println("DAte::: "+ Instant.now().truncatedTo(ChronoUnit.SECONDS));
        System.out.println("Date::: "+ new Date());

        if (!expiryDateString.endsWith("Z")) {
            expiryDateString += "Z";
        }

        Instant expiryDate = null;
        try {
            expiryDate = Instant.parse(expiryDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(expiryDate.compareTo(Instant.now()) < 0);
        System.out.println(expiryDate);
        System.out.println(Instant.now());

        if(expiryDate.compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException(token.get("token").toString(), "Token is expired");
        }
        return true;
    }
//
//    import java.time.Instant;
//import java.time.format.DateTimeParseException;
//import java.util.Map;
//
//    public boolean verifyRefreshToken(Map<String, Object> token) throws TokenRefreshException {
//        String expiryDateString = token.get("expiry_date").toString();
//
//        // Add 'Z' to represent UTC if not already present in the date-time string
//        if (!expiryDateString.endsWith("Z")) {
//            expiryDateString += "Z";
//        }
//
//        Instant expiryDate;
//        try {
//            expiryDate = Instant.parse(expiryDateString);
//        } catch (DateTimeParseException e) {
//            throw new TokenRefreshException(token.get("token").toString(), "Invalid expiry date format");
//        }
//
//        Instant now = Instant.now();
//
//        if (expiryDate.isBefore(now)) {
//            throw new TokenRefreshException(token.get("token").toString(), "Token is expired");
//        }
//
//        return true;
//    }


//    import java.time.Instant;
//import java.time.format.DateTimeParseException;
//import java.util.Map;
//
//    public boolean verifyRefreshToken(Map<String, Object> token) throws TokenRefreshException {
//        String expiryDateString = token.get("expiry_date").toString();
//
//        Instant expiryDate;
//        try {
//            expiryDate = Instant.parse(expiryDateString);
//        } catch (DateTimeParseException e) {
//            throw new TokenRefreshException(token.get("token").toString(), "Invalid expiry date format");
//        }
//
//        Instant now = Instant.now();
//
//        if (expiryDate.isBefore(now)) {
//            throw new TokenRefreshException(token.get("token").toString(), "Token is expired");
//        }
//
//        return true;
//    }


    public boolean verifyRefreshToken2(Map<String, Object> token) throws TokenRefreshException {
        String expiryDateString = token.get("expiry_date").toString();

        Instant expiryDate = Instant.parse(expiryDateString);

        if (expiryDate.isBefore(Instant.now())) {
            throw new TokenRefreshException(token.get("token").toString(), "Token is expired");
        }

        return true;
    }

}
