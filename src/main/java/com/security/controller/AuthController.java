package com.security.controller;

import com.security.config.MyUserDetailsService;
import com.security.model.AppResponse;
import com.security.model.User;
import com.security.repo.AuthRepo;
import com.security.utils.AppUtils;
import com.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

        if(username.trim().equals("")) {
            return AppResponse.build(HttpStatus.NOT_ACCEPTABLE).message("Username cannot be empty");
        }

        if(password.trim().equals("")) {
            return AppResponse.build(HttpStatus.NOT_ACCEPTABLE).message("Password cannot be empty");
        }

        return loginExecute(username.trim(), password);

    }

    public AppResponse loginExecute(String username, String password) {

        Map<String, Object> user = new HashMap<>();

        try {
            user = authRepo.findByUserName(username);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AppResponse.build(HttpStatus.INTERNAL_SERVER_ERROR).message("User not valid");
        }

        if(!passwordEncoder.matches(password, AppUtils.toString(user.get("password"))) && AppUtils.getLong(user, "user_id")!=0) {
            return AppResponse.build(HttpStatus.UNAUTHORIZED).message("Username/Password is invalid");
        }


        if(user != null) {
            User u = new User();
            u.setUsername(AppUtils.toString(user.get("username")));
            u.setEmail(AppUtils.toString(user.get("email")));
            u.setUser_id(AppUtils.toLong(user.get("user_id")));
            u.setRole(AppUtils.toString("role"));
            u.setNumber(AppUtils.toString("number"));

            String token = jwtUtils.generateToken(u);
            System.out.println("token:::: " + token);

            Map<String, Object> params = new HashMap<>();
            params.put("Token", token);

            return AppResponse.build(HttpStatus.OK).body(user).header(params).message("Welcome To Coding World.");
        } else {
            return  AppResponse.build(HttpStatus.UNAUTHORIZED).message("Not a valid user");
        }
    }

}
