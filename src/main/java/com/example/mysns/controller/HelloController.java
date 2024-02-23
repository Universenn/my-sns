package com.example.mysns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/hello")
@ApiIgnore
public class HelloController {
    @GetMapping()
    public String hello() {
        return "hello5";
    }
}