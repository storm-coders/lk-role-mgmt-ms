package com.vcgdev.demo.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("greeting")
public class ExampleRestController {
    
    
    @GetMapping
    @PreAuthorize("permitAll()")
    public HttpEntity<Map<String, String>> securedGreeting() {
        Map<String, String> greetings = new HashMap<>();
        greetings.put("greeting", "Hi, from secured endpoint");
        return new ResponseEntity<>(greetings, HttpStatus.OK);
    }
}
