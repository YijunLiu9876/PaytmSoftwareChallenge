package com.paytm.core.service;

import com.paytm.core.domain.CustomAuthorityEnum;
import com.paytm.core.domain.UserModel;
import com.paytm.core.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserModelService {
    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    public boolean userExists(String username) {
        return userModelRepository.existsById(username);
    }

    public void createUser(UserModel userModel) {
        Set<CustomAuthorityEnum> authorities = new HashSet<>();
        authorities.add(CustomAuthorityEnum.ROLE_APPUSER);
        userModel.setAuthorities(authorities);
        userModel.setPassword(encoder.encode(userModel.getPassword()));
        userModelRepository.save(userModel);
    }

    public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
            Set<CustomAuthorityEnum> authorities = new HashSet<>();
            authorities.add(CustomAuthorityEnum.ROLE_APPUSER);
            UserModel userModel = new UserModel(username, encoder.encode(newPassword), authorities);
            userModelRepository.save(userModel);
        }
    }

    public UserModel findUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userModel = userModelRepository.findTopByUsername(username);
        if (!userModel.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return userModel.get();
    }
}
