package com.project.AlexIad.controller;

import com.project.AlexIad.exceptions.NotFoundException;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
            return new ResponseEntity<String>("Success registration!", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Error, please , try again :(", HttpStatus.EXPECTATION_FAILED);
    }

    @GetMapping("/confirm/{code}")
    public ResponseEntity<Object> confirm(@PathVariable String code) throws URISyntaxException {
        if (userService.activationAccount(code)) {
            String mes =  "Success, your account is activated !";
            URI reactApp = new URI("http://localhost:3000/confirm/"+ code);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(reactApp);
            return new ResponseEntity<>(mes,httpHeaders,HttpStatus.SEE_OTHER);
        }
        String mes2 =  " Error! Your code is not exist or not active more :(";
//        URI reactApp = new URI("http://localhost:3000/confirm/"+ code);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(reactApp);
        return new ResponseEntity<>(mes2,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/checkloginAndEmail")
    public ResponseEntity<String> checkingData (@RequestBody User user)
   {
        return userService.IsPresentLoginOrEmail(user.getUsername(), user.getEmail());
    }




//    @GetMapping("/auth")
//         String resp (){
//        return "Response";
//        }
    }


