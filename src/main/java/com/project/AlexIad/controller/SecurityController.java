package com.project.AlexIad.controller;

import com.project.AlexIad.exceptions.NotFoundException;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SecurityController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;


    @PostMapping("/registration")
    public ResponseEntity<String> registrationNewUser(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userService.saveUser(user)) {
            return new ResponseEntity<String>("Success,  new user is saved!", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Error,  request body is not correct!", HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping("/confirm/{code}")
    public String confirm(@PathVariable String code) {
        if (userService.activationAccount(code)) {
            return "Success, your account is activated !";
        }
        return " Error! Your code is not exist or not active more :(";
    }


}
