package com.paytm.core.service;

import java.io.IOException;
import java.util.List;

public interface TweetsServiceInterface {
    List<String> searchTweets(String queryStr) throws IOException;
    List<String> searchTweetsNeverViewed(String queryStr) throws IOException;
}
