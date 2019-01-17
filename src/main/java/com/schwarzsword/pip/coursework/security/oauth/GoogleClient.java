package com.schwarzsword.pip.coursework.security.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import java.util.ArrayList;

@EnableOAuth2Sso
@Configuration
public class GoogleClient {

    @Bean
    public OAuth2ProtectedResourceDetails googleResourceDetails() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        String clientId = "842951186062-0iro1ed7cv5jv0i7giaqobrk865vf0ie.apps.googleusercontent.com";
        details.setClientId(clientId);
        String clientSecret = "YhFhqj9shOf5GjnHmdYLODxS";
        details.setClientSecret(clientSecret);
        String accessTokenUri = "https://www.googleapis.com/oauth2/v3/token";
        details.setAccessTokenUri(accessTokenUri);
        String userAuthorizationUri = "https://accounts.google.com/o/oauth2/auth";
        details.setUserAuthorizationUri(userAuthorizationUri);
        String redirectUri = "http://localhost:10880/login/google";
        details.setPreEstablishedRedirectUri(redirectUri);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("email");
        strings.add("openid");
        strings.add("profile");
        details.setScope(strings);
        details.setUseCurrentUri(false);
        return details;
    }

    @Bean
    public OAuth2RestTemplate googleRestTemplate(OAuth2ProtectedResourceDetails googleResourceDetails, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(googleResourceDetails, context);
    }
}