package com.paytm.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class ServiceConfig {

    @Bean
    public Twitter twitter(Environment env){
        return new TwitterTemplate(env.getProperty("twitter.consumerKey"),
                env.getProperty("twitter.consumerSecret"));
    }

}
