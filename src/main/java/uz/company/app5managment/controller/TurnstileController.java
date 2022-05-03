package uz.company.app5managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.company.app5managment.entity.Turnstile;
import uz.company.app5managment.repository.TurnstileRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/turnstile")
public class TurnstileController {

    @Autowired
    TurnstileRepository turnstileRepository;

    @GetMapping("/byUserId/{userId}")
    public HttpEntity<?> getTurnstile(@PathVariable UUID userId){
        List<Turnstile> allByUser_id = turnstileRepository.findAllByUser_Id(userId);
        return ResponseEntity.ok(allByUser_id);
    }



}
