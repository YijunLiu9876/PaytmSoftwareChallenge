package com.paytm.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/HelloWorld")
public class HelloWorldController {
    @GetMapping
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().body("Hello World");
    }
}
