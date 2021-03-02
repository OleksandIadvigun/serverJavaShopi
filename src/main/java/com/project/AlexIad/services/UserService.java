package com.project.AlexIad.services;

import com.project.AlexIad.dao.UserDAO;
import com.project.AlexIad.exceptions.NotFoundException;
import com.project.AlexIad.models.Product;
import com.project.AlexIad.models.User;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        try{
            List<Product> productList =  user.getProducts();
            productList.stream().forEach(item ->item.setOverdueDate(
                    item.getCreationDate().plusDays(item.getExpiration())));
        }
        catch (NotFoundException e){
            System.out.println(e.getMessage());
        }
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

    public void saveCleanUser(User user){
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
      User user =   userDAO.findUserByUsername(login);
      return user;
    }

//    public User findByLoginAndPassword(String login, String password) {
//        User user = findUserByLogin(login);
//        if (user != null) {
//            if (passwordEncoder.matches(password, user.getPassword())) {
//                return user;
//            }
//        }
//        return null;
//    }

    public User findUserByToken(String token) {
        return userDAO.findUserByToken(token);
    }
}


