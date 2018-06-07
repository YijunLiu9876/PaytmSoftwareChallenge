package com.paytm.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class TwitterClientConfig {
    @Value("${twitter.accessTokenUri}")
    private String accessTokenUri;

    @Value("${twitter.consumerKey}")
    private String clientID;

    @Value("${twitter.consumerSecret}")
    private String clientSecret;

    @Bean
    public OAuth2ProtectedResourceDetails twitter() {
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setClientId(clientID);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        return details;
    }

    @Bean
    public OAuth2RestTemplate twitterRestTemplate() {
        OAuth2RestTemplate template = new OAuth2RestTemplate(twitter(), new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
        AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(
                Arrays.<AccessTokenProvider>asList(
                        new ClientCredentialsAccessTokenProvider())
        );
        template.setAccessTokenProvider(accessTokenProvider);
        return template;
    }

}
