package com.security.controller;

import com.security.config.MyUserDetailsService;
import com.security.model.AppResponse;
import com.security.model.RefreshToken;
import com.security.model.User;
import com.security.payload.RefreshTokenRequest;
import com.security.repo.AuthRepo;
import com.security.repo.RefreshTokenRepo;
import com.security.service.RefreshTokenService;
import com.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private RefreshTokenService tokenService;

    @Autowired
    private JwtUtils jwtUtils;

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ResponseEntity loginSubmit(@RequestBody User user, HttpServletResponse response) {
//
//        System.out.println(user.getUsername() + " :::::: " + user.getPassword());
//
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//        } catch (Exception e) {
//e.printStackTrace();
//        }
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//
//        String token = jsJwtUtils.generateToken(user);
//        System.out.println("token:::: "+token);
//
////
////    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
////
////    String token = jwtUtils.generateToken(userDetails);
//
//    response.addHeader("Token", token);
//
//        return new ResponseEntity("token" + token, HttpStatus.OK);
//
//    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AppResponse loginSubmit(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {

        System.out.println(username + " :::::: " + password);

        User user = authRepo.findByUserName(username);

        return loginExecute(user);

    }

    public AppResponse loginExecute(User user) {

        if(user != null) {
            String token = jwtUtils.generateToken(user);
            System.out.println("token:::: " + token);

            RefreshToken refreshToken = tokenService.createRefreshToken(user.getUser_id());

            Map<String, Object> params = new HashMap<>();
            params.put("Token", token);
            params.put("RefreshToken", refreshToken.getToken());

            return AppResponse.build(HttpStatus.OK).body(user).header(params);
        } else {
            return  AppResponse.build(HttpStatus.UNAUTHORIZED).message("Not a valid user");
        }
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
//    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public AppResponse responseToken(@RequestBody RefreshTokenRequest request) throws Exception {

        String getRefreshToken = request.getRefreshToken();

         Map<String, Object> tokenInfo = tokenService.findByToken(getRefreshToken);

         if(!tokenInfo.isEmpty()) {
//             if(tokenService.verifyRefreshToken(tokenInfo)) {

                 User user = new User();
                 user.setUsername(tokenInfo.get("username").toString());
                 user.setUser_id(1L);
                 user.setRole("ROLE_ADMIN");

                 String token = jwtUtils.generateToken(new HashMap<>(), user);

                 Map<String, Object> params = new HashMap<>();
                 params.put("token", token);
                 params.put("refreshToken", getRefreshToken);

                 return AppResponse.build(HttpStatus.OK).body(user).header(params);
//             }
         }

        return AppResponse.build(HttpStatus.UNAUTHORIZED).message("Refresh Token is not in the database!!");
    }

}
