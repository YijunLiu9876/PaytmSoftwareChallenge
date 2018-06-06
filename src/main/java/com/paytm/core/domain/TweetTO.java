package com.paytm.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TweetTO {
    private String id;
    private String username;
    private String text;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(username);
        sb.append(":");
        sb.append(text);
        return sb.toString();
    }
}
