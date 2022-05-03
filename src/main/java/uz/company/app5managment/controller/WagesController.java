package uz.company.app5managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.company.app5managment.entity.User;
import uz.company.app5managment.entity.Wages;
import uz.company.app5managment.entity.WagesDto;
import uz.company.app5managment.payload.ApiResponse;
import uz.company.app5managment.repository.UserRepository;
import uz.company.app5managment.repository.WagesRepository;
import uz.company.app5managment.service.WagesService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/wages")
public class WagesController {

    @Autowired
    WagesService wagesService;

    @PostMapping
    public HttpEntity<?> addWages(@RequestBody WagesDto wagesDto) {
        ApiResponse apiResponse = wagesService.addWages(wagesDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/byUserId/{userId}")
    public HttpEntity<?> getWagesByUserId(@PathVariable UUID userId) {
        List<Wages> wages = wagesService.getWagesByUserId(userId);
        return ResponseEntity.ok(wages);
    }

}
