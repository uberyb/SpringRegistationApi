package com.example.restservice.register;

import com.example.restservice.register.model.UserProfile;
import com.example.restservice.register.model.ApiResponse;

public interface RegisterService {
    public ApiResponse createUser(UserProfile user);
    public ApiResponse activateUser(UserProfile user);
}