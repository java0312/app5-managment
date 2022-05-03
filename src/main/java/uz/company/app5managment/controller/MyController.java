package uz.company.app5managment.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my")
public class MyController {

    @GetMapping
    public HttpEntity<?> getMy(){
        return ResponseEntity.ok("This is for me!");
    }

}
