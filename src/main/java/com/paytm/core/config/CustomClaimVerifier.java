package com.paytm.core.config;

import com.paytm.core.domain.UserModel;
import com.paytm.core.service.UserModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

import java.sql.Timestamp;
import java.util.Map;

@Slf4j
public class CustomClaimVerifier implements JwtClaimsSetVerifier {

    @Autowired
    UserModelService userModelService;

    @Override
    public void verify(Map<String, Object> claims) throws InvalidTokenException {
        Timestamp creationTimestamp = new Timestamp((Long) claims.get("creation_timestamp"));
        String username = (String) claims.get("user_name");
        log.info("creationTimestamp from claims: " + creationTimestamp);
        if ((creationTimestamp == null || username == null || username.length() == 0)) {
            throw new InvalidTokenException("missing claims");
        }
        try {
            UserModel userModel = userModelService.findUserByUsername(username);
            if ((creationTimestamp.before(userModel.getLastModifiedDate())))
                throw new InvalidTokenException("token invalid");
        } catch (UsernameNotFoundException unfe) {
            throw new InvalidTokenException(unfe.getMessage());
        }
    }
}