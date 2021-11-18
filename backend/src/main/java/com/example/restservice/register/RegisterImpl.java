package com.example.restservice.register;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.example.restservice.register.model.UserProfile;
import com.example.restservice.register.model.ApiResponse;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserActivationToken;
import com.okta.sdk.resource.user.PasswordCredential;
import com.okta.sdk.resource.user.UserCredentials;
import org.springframework.core.env.Environment;

import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;

import com.okta.sdk.resource.ExtensibleResource;

import org.springframework.beans.factory.annotation.Autowired;

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
        try {
            User user = UserBuilder.instance()
                    .setEmail(profile.getEmail())
                    .setActive(true)
                    .buildAndCreate(this.client);

//            UserActivationToken token = user.activate(true);

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
    }

    public ApiResponse activateUser(UserProfile profile) {

        ExtensibleResource resource = client.instantiate(ExtensibleResource.class);
        resource.put("token", profile.getToken());
        ExtensibleResource result = client.instantiate(ExtensibleResource.class);
        try {
            result = this.client.http().setBody(resource).post("/api/v1/authn", ExtensibleResource.class);
        } catch (ResourceException e) {
            ApiResponse r = new ApiResponse()
                    .setMsg("Bad token provided.");
            return r;
        }
        String embeddedResponse = result.getString("_embedded");
        int index1 = embeddedResponse.indexOf("id=") + 3;
        int index2 = embeddedResponse.substring(index1).indexOf(",");
        String userId = embeddedResponse.substring(index1).substring(0,index2);

        UserProfile returnedUser = new UserProfile()
                .setId(userId);

        User user = client.getUser(userId);

        if (user.getProfile().getLogin() == profile.getEmail()) {
            ;
        } else {
            user.getProfile().setLogin(profile.getEmail());
        }



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