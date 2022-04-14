package com.example.login.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest registrationRequest) {
        return registrationService.register(registrationRequest);
    }

    @GetMapping // http://localhost:8080/api/registration?token=abcdef123456789
    public String confirmUser(@RequestParam("token") String token) {
        return registrationService.confirmUser(token);
    }

}