package com.joanna.staybooking.controller;

import com.joanna.staybooking.model.User;
import com.joanna.staybooking.model.UserRole;
import com.joanna.staybooking.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController   //默认 @Controller @ResponseBody 合并在一起
public class RegisterController {
    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    //@PostMapping(“/register/guest”) annotation to indicate the API supports POST method and maps to the /register/guest path
    @PostMapping("/register/guest")
    public void addGuest(@RequestBody User user) { //@RequestBody annotation to help convert the request body from JSON format to a Java object
        registerService.add(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/register/host")
    public void addHost(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_HOST);
    }
}
