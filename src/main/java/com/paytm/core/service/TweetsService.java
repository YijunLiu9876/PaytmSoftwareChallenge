package com.paytm.core.service;

import com.paytm.core.domain.TweetDomain;
import com.paytm.core.domain.UserEvent;
import com.paytm.core.repository.UserEventRepository;
import com.paytm.core.service.client.TwitterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetsService implements TweetsServiceInterface {

    @Autowired
    UserEventRepository userEventRepository;

    @Autowired
    TwitterClient twitterClient;

    @Override
    public List<String> searchTweets(String queryStr) throws IOException {
        List<TweetDomain> tweets = twitterClient.search(queryStr);
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return tweets.stream().parallel().map(tweet -> {
            userEventRepository.insert(new UserEvent(currentUser.getName(), tweet.getIdStr()));
            return new TweetDomain(tweet.getIdStr(), tweet.getUsername(), tweet.getText()).toString();
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> searchTweetsNeverViewed(String queryStr) throws IOException {
        List<TweetDomain> tweets = twitterClient.search(queryStr);
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return tweets.stream().parallel()
                .filter( tweet -> userEventRepository.exists(currentUser.getName(), tweet.getIdStr()) == 0)
                .map(filterResult -> {
            userEventRepository.save(new UserEvent(currentUser.getName(), filterResult.getIdStr()));
            return new TweetDomain(filterResult.getIdStr(), filterResult.getUsername(), filterResult.getText()).toString();
        }).collect(Collectors.toList());
    }

}
