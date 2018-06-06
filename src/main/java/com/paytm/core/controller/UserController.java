package com.paytm.core.controller;

import com.paytm.core.domain.UserModel;
import com.paytm.core.service.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserModelService userModelService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (userModelService.userExists(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("username exist");
        }
        userModelService.createUser(new UserModel(username, password));
        return ResponseEntity.accepted().body("user created");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        try {
            userModelService.changePassword(oldPassword, newPassword);
        } catch (AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ae.getMessage());
        }
        return ResponseEntity.ok().body("password changed, client needs to logout and clear current access_token");
    }
}
