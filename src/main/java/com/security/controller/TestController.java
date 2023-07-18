package com.security.controller;

import com.security.model.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class TestController {

    @GetMapping("/test")
    public AppResponse testAdmin() {
        return AppResponse.build(HttpStatus.OK).body("Test Admin Role");
    }

    @RequestMapping(value="/testAdmin", method = RequestMethod.GET)
    public AppResponse adminPing(){
        return AppResponse.build(HttpStatus.OK).body("Only Admins Can Read This");
    }

}
