package com.security.controller;

import com.security.config.MyUserDetailsService;
import com.security.model.AppResponse;
import com.security.model.User;
import com.security.repo.AuthRepo;
import com.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthRepo authRepo;

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

            Map<String, Object> params = new HashMap<>();
            params.put("Token", token);

            return AppResponse.build(HttpStatus.OK).body(user).header(params);
        } else {
            return  AppResponse.build(HttpStatus.UNAUTHORIZED).message("Not a valid user");
        }
    }

}
