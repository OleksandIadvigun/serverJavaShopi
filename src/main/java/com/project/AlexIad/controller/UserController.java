package com.project.AlexIad.controller;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable int id, @RequestHeader("Authorization") String header ){
        User userById = userService.findUserById(id, header);
        if(userById!=null){
        return new ResponseEntity<User>(userById, HttpStatus.OK);
        }
      return new ResponseEntity<User>( HttpStatus.NOT_FOUND);
    }
    @PutMapping("edit")
    public ResponseEntity<User> editUser(@RequestBody User user,
                                         @RequestHeader("Authorization") String header){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User editedUser = userService.editUser(user, header);
         if(editedUser!=null){
           return  new ResponseEntity<User>(editedUser, HttpStatus.OK);
         }
         return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }
}

