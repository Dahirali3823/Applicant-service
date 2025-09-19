package com.example.health;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController

public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<Map<String,String>> status(){
        return ResponseEntity.ok(Map.of("status", "READY", "message", "Service is operational"));
    }
}