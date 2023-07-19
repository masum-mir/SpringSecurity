package com.security.controller;

import com.security.model.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
public class UserController {

//    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/testUser", method = RequestMethod.GET)
    public AppResponse adminPing(){
        return AppResponse.build(HttpStatus.OK).body("User Can Read This");
    }

//    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public AppResponse userPing(){
        return AppResponse.build(HttpStatus.OK).body("Any User Can Read This");
    }

}
