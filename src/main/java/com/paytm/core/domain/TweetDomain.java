package com.paytm.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetDomain {
    @JsonProperty(value="id_str")
    private String idStr;

    private String username;
    @JsonProperty(value="text")
    private String text;

    @JsonProperty(value="user")
    private void unpackNameFromNestedObject(Map<String, Object> user) {
        username = (String) user.get("name");
    }

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
