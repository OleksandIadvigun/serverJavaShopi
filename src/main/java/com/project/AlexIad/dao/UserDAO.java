package com.project.AlexIad.dao;

import com.project.AlexIad.models.User;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


@EnableJdbcRepositories
public interface UserDAO extends JpaRepository<User, Long> {
    @Query(name = "get_user",                                                 // <-------------- Solved Lazy exception!
           value = "SELECT u FROM User u  left JOIN fetch u.shops where u.username= :username ")
    User findUserByUsername(@Param("username") String username);


    User findUserById(int id);
    User findUserByToken(String token);
    User findUserByActivationCode(String code);
    List<User> findAll();

}
