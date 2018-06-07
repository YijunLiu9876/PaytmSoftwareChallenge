package com.paytm.core.controller;

import com.paytm.core.service.TweetsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tweets")
public class TweetsController {

    @Autowired
    TweetsServiceInterface tweetsService;

    @GetMapping("/search/{query-str}")
    public ResponseEntity<List<String>> searchTweets(@PathVariable("query-str") String queryStr) {
        List<String> tweets = null;
        try {
            tweets = tweetsService.searchTweets(queryStr);
            if (tweets.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(tweets);
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/lucky/{query-str}")
    public ResponseEntity<List<String>> searchTweetsNeverViewed(@PathVariable("query-str") String queryStr) {
        try {
            List<String> tweets = tweetsService.searchTweetsNeverViewed(queryStr);
            if (tweets.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(tweets);
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
