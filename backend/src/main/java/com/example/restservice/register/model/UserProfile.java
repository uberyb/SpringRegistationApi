package com.example.restservice.register.model;

public class UserProfile {
    private String email;
    private String token;
    private String id;
    private String password;
//    public UserProfile(String email, String firstName, String lastName) {
//        this.email = email;
//    }
    // Gets
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }


    public String getPassword() {
        return password;
    }

    // Sets

//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }

    public UserProfile setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserProfile setToken(String token) {
        this.token = token;
        return this;
    }

    public UserProfile setId(String id) {
        this.id = id;
        return this;
    }

}