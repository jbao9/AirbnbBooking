package com.joanna.staybooking.controller;

import com.joanna.staybooking.model.Token;
import com.joanna.staybooking.model.User;
import com.joanna.staybooking.model.UserRole;
import com.joanna.staybooking.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController   //@RestController：把@controller 和 @responseBody结合起来了
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired  //dependency injection
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate/guest")  //定义post api: POST method + url
    public Token authenticateGuest(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/authenticate/host")
    public Token authenticateHost(@RequestBody User user) {
        return authenticationService.authenticate(user, UserRole.ROLE_HOST);
    }
}
