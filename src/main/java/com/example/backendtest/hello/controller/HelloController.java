package com.example.backendtest.hello.controller;

import com.example.backendtest.hello.authentication.JwtAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(JwtAuthentication principal) {
        return "Hello " + principal.getUsername();
    }
}
