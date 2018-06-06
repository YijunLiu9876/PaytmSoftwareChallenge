package com.paytm.core.service;

import java.util.List;

public interface TweetsServiceInterface {
    List<String> searchTweets(String queryStr);
    List<String> searchTweetsNeverViewed(String queryStr);
}
