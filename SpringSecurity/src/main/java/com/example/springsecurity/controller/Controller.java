package com.example.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/login")
    public String hello(){
        return "Hello";
    }

    @GetMapping("/another-api")
    public String anotherAPI(){
        return "API";
    }
}
