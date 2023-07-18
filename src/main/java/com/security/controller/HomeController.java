package com.security.controller;

import com.security.model.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class HomeController {

    @GetMapping("/test")
    public AppResponse getString() {

        return AppResponse.build(HttpStatus.OK).body("data found").message("text message");
    }

}
