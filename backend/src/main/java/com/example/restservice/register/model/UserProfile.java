package com.example.restservice.register.model;

public class UserProfile {
  private String email, firstName, lastName, token, id, password, ssn, login;

  public UserProfile(String email, String firstName, String lastName) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.login = login;

  }

  // Gets
  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

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

  public String getSsn() {
    return ssn;
  }

  public String getLogin() {
    return login;
  }

  // Sets

  public UserProfile setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public UserProfile setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public UserProfile setLogin(String login) {
    this.login = login;
    return this;
  }

  public UserProfile setSsn(String ssn) {
    this.ssn = ssn;
    return this;
  }

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
