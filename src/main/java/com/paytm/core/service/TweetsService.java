package com.paytm.core.service;

import com.paytm.core.domain.TweetTO;
import com.paytm.core.domain.UserEvent;
import com.paytm.core.repository.UserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetsService implements TweetsServiceInterface {

    @Autowired
    Twitter twitter;

    @Autowired
    UserEventRepository userEventRepository;

    private final String TWEETS_URL = "https://api.twitter.com/1.1/search/tweets.json";

    @Override
    public List<String> searchTweets(String queryStr) {

        SearchResults results = twitter.searchOperations().search(queryStr);
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return results.getTweets().stream().parallel().map(result -> {
            userEventRepository.insert(new UserEvent(currentUser.getName(), result.getIdStr()));
            return new TweetTO(result.getIdStr(), result.getUser().getName(), result.getText()).toString();
    }).collect(Collectors.toList());
    }

    @Override
    public List<String> searchTweetsNeverViewed(String queryStr) {
        SearchResults results = twitter.searchOperations().search(queryStr);
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        return results.getTweets().stream().parallel()
                .filter( result -> userEventRepository.exists(currentUser.getName(), result.getIdStr()) == 0)
                .map(filterResult -> {
            userEventRepository.save(new UserEvent(currentUser.getName(), filterResult.getIdStr()));
            return new TweetTO(filterResult.getIdStr(), filterResult.getUser().getName(), filterResult.getText()).toString();
        }).collect(Collectors.toList());
    }

}
