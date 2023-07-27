package com.security.controller;

import com.security.config.AuthenticationFilter;
import com.security.config.MyUserDetails;
import com.security.config.MyUserDetailsService;
import com.security.dto.UserMDto;
import com.security.model.AppResponse;
import com.security.model.UserM;
import com.security.repo.AuthRepo;
import com.security.repo.UserMRepo;
import com.security.service.UserService;
import com.security.utils.AppUtils;
import com.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

//    @Autowired
//    private AuthRepo authRepo;

    @Autowired
    private UserMRepo userMRepo;

    @Autowired
    private UserService userService;

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

//        try {
//            user = (Map<String, Object>) userService.findByUserName(username);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return AppResponse.build(HttpStatus.INTERNAL_SERVER_ERROR).message("User not valid");
//        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        if(!passwordEncoder.matches(password, AppUtils.toString(user.get("password"))) && AppUtils.getLong(user, "user_id")!=0) {
            return AppResponse.build(HttpStatus.UNAUTHORIZED).message("Username/Password is invalid");
        }

        if(user != null) {
            UserMDto u = new UserMDto();
            u.setUsername(AppUtils.toString(userDetails.getUsername()));
            u.setEmail(AppUtils.toString(user.get("email")));
            u.setUser_id(AppUtils.toLong(user.get("user_id")));
//            u.setRole(AppUtils.toString(user.get("role")));
//            u.setRoles();
            u.setNumber(AppUtils.toString(user.get("number")));

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
