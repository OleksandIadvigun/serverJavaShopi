package com.project.AlexIad.services;

import com.project.AlexIad.exceptions.NotFoundException;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.dao.UserDAO;
import com.project.AlexIad.models.Shop;
import com.project.AlexIad.models.User;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private UserDAO userDAO;
    private Environment env;
    private MailSenderService mailSenderService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public boolean saveUser(User user) {
        // user.setRole(user.getRole());
//        try {
//            List<Product> productList = user.getProducts();
//            productList.stream().forEach(item -> item.setOverdueDate(
//                    item.getCreationDate().plusDays(item.getExpiration())));
//        } catch (NotFoundException e) {
//            System.out.println(e.getMessage());
//        }
        user.setActivationCode(UUID.randomUUID().toString());
        if (user.getEmail() != null) {
            String message = String.format("Hello, %s! \n" +
                            "Welcome to SHOPI Application ! Please, click next link to activate your account: %s/confirm/%s",
                    user.getUsername(),
                    env.getProperty("mail.hostName"),
                    user.getActivationCode()
            );
            mailSenderService.send(user.getEmail(), message);
            userDAO.save(user);
            return true;
        }
        return false;
    }

    public void saveCleanUser(User user) {
        userDAO.save(user);

    }

    public boolean activationAccount(String code) {
        User user = userDAO.findUserByActivationCode(code);
        if (user != null) {
            user.setActivationCode(null);
            user.setActivated(true);
            userDAO.save(user);
            return true;
        }
        return false;
    }

    public User findUserByLogin(String login) {
        User user = userDAO.findUserByUsername(login);
        return user;
    }


    public User findUserByToken(String token) {
        return userDAO.findUserByToken(token);
    }

    public User findUserById(int id, String header) {
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if (userFromDB.getId() == id) {
                return userDAO.findUserById(id);
            }
        }
        return null;
    }

    public ResponseEntity<String> IsPresentLoginOrEmail(String username, String email) {
        List<User> all = userDAO.findAll();
        try {
        List<User> anyLogin = all.stream().filter(user -> user.getUsername().equals(username)).collect(Collectors.toList());
            if (anyLogin.size() != 0) {
                System.out.println("Username is PRESENT!");
                return new ResponseEntity<String>("Username is already exist!", HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            List<User> anyEmail = all.stream().filter(user -> user.getEmail().equals(email)).collect(Collectors.toList());
            if (anyEmail.size() != 0) {
                return new ResponseEntity<String>("Email is already exist!", HttpStatus.OK);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public User editUser(User user, String header){
        if(header!=null) {
            String clearToken = header.substring(7);
            User userFromDB = userDAO.findUserByToken(clearToken);
            if (userFromDB!=null) {
                BeanUtils.copyProperties(user, userFromDB, "id","token","shops","products","isActivated");
                userFromDB.setActivated(true);
                userDAO.save(userFromDB);
                userFromDB.setPassword("");
                return userFromDB;
            }
        }
        return null;
    }
}


