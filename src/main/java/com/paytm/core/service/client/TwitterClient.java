package com.paytm.core.service.client;

import com.paytm.core.domain.SearchResults;
import com.paytm.core.domain.TweetDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class TwitterClient {

    @Autowired
    OAuth2RestTemplate twitterRestTemplate;

    @Value("${twitter.tweetsSearchUrl}")
    private String TWEETS_URL;

    public List<TweetDomain> search(String queryStr) throws IOException {
        SearchResults searchResults = twitterRestTemplate.getForObject(TWEETS_URL + "?q=" + queryStr, SearchResults.class);
        log.info(searchResults.getTweets().toString());
        return searchResults.getTweets();
    }

}
