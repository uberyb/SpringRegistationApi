package com.example.restservice.register;

import java.util.Map;
import javax.servlet.http.HttpSession;

import com.example.restservice.register.model.UserProfile;
import com.example.restservice.register.model.ApiResponse;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserActivationToken;
import com.okta.sdk.resource.user.PasswordCredential;
import com.okta.sdk.resource.user.UserCredentials;
import com.okta.sdk.resource.ExtensibleResource;
import com.okta.sdk.resource.ResourceException;

@Service
public class RegisterImpl implements RegisterService {

  private String orgUrl;
  private String apiToken;
  private Client client;

  @Autowired
  public RegisterImpl(@Value("${org.url}") String url, @Value("${org.apiToken}") String apiToken) {
    this.orgUrl = url;
    this.apiToken = apiToken;
    this.client = Clients.builder()
        .setOrgUrl(this.orgUrl)
        .setClientCredentials(new TokenClientCredentials(this.apiToken))
        .build();
  }

  public ApiResponse createUser(UserProfile profile) {

    UserList users = this.client.listUsers(null, null, "profile.ssn eq \"" + profile.getSsn() + "\"", null, null);
    try {
      User user = users.single();

      // Before performing an update, we should also probably check which fields are
      // null and which are not.
      System.out.println(profile.getFirstName());
      user.getProfile().setFirstName(profile.getFirstName()) // Firstname and lastName are not being updated.
          .setLastName(profile.getLastName())
          .setEmail(profile.getEmail())
          .setLogin(profile.getLogin());
      user.update();
      ApiResponse r = new ApiResponse()
          .setMsg("User updated.");
      return r;
      // continue on with update flow.
    } catch (java.lang.IllegalStateException ill) {
      // this exception means that we need to create the user.
      try {
        User user = UserBuilder.instance()
            .setEmail(profile.getEmail())
            .setActive(true)
            .buildAndCreate(this.client);

        Map<String, String> data = Map.of("email", profile.getEmail());
        ApiResponse r = new ApiResponse()
            .setData(data);
        return r;
      } catch (ResourceException e) {
        System.out.println(e.getError());
        ApiResponse r = new ApiResponse()
            .setMsg(e.getError().getMessage());

        return r;
      }
    } catch (ResourceException e) {
      ApiResponse r = new ApiResponse()
          .setMsg(e.getError().getMessage());
      return r;
    }

    // Attempt to create a user. On failure, grab the error message and return it.
  }

  public ApiResponse activateUser(UserProfile profile) {

    /*
     * In this block we will need to put local validation of password and username
     * requirements.
     */

    ExtensibleResource resource = client.instantiate(ExtensibleResource.class);
    resource.put("token", profile.getToken());
    ExtensibleResource result = client.instantiate(ExtensibleResource.class);
    try {
      result = this.client.http().setBody(resource).post("/api/v1/authn", ExtensibleResource.class);
    } catch (ResourceException e) {
      ApiResponse r = new ApiResponse()
          .setMsg("Bad activation token provided.");
      return r;
    }

    // Get the Okta user id from the response. It would be better if we could parse
    // JSON directly but I am retrieving it as a String here.
    String embeddedResponse = result.getString("_embedded");
    int index1 = embeddedResponse.indexOf("id=") + 3;
    int index2 = embeddedResponse.substring(index1).indexOf(",");
    String userId = embeddedResponse.substring(index1).substring(0, index2);

    User user = client.getUser(userId);

    if (user.getProfile().getLogin() == profile.getEmail()) {
      ;
    } else {
      user.getProfile().setLogin(profile.getEmail());
    }

    // Set updated credentials.
    PasswordCredential pwCred = client.instantiate(PasswordCredential.class);
    pwCred.setValue(profile.getPassword().toCharArray());
    user.getCredentials().setPassword(pwCred);

    try {
      user.update();
      Map<String, String> data = Map.of("email", user.getProfile().getEmail(), "login", user.getProfile().getLogin());
      ApiResponse r = new ApiResponse()
          .setData(data);
      return r;

    } catch (ResourceException e) {
      System.out.println(e);
      ApiResponse r = new ApiResponse()
          .setMsg(e.getError().getMessage());
      return r;
    }

  }

}
