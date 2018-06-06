package com.paytm.core.service;

import com.paytm.core.config.CustomUserPrinciple;
import com.paytm.core.domain.UserModel;
import com.paytm.core.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserModelRepository userModelRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userModel = userModelRepository.findTopByUsername(username);
        if (!userModel.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserPrinciple(userModel.get());
    }


}
