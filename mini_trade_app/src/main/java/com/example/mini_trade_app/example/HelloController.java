package com.example.mini_trade_app.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello Health Check";
    }
}