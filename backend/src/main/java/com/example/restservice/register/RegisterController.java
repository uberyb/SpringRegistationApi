package com.example.restservice.register;

import javax.servlet.http.HttpSession;

import com.example.restservice.register.model.UserProfile;
import com.example.restservice.register.model.ApiResponse;
import com.example.restservice.register.RegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin // can specify a specific host later if I want to.
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ApiResponse createUser(@RequestBody UserProfile profile) {
        return registerService.createUser(profile);
    }

    @PostMapping(value = "/activate", consumes = "application/json", produces = "application/json")
    public ApiResponse activateUser(@RequestBody UserProfile profile) {
        return registerService.activateUser(profile);
    }
}