package com.example.springsecurity.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class Controller {

    @GetMapping("/login")
    public String hello(){
        log.info("Api được bỏ qua authentication");

        return "Hello";
    }

    @GetMapping("/another-api")
    public String anotherAPI(){
        log.info("Tiếp tục vào controller thực hiện service");

        return "API";
    }
}
