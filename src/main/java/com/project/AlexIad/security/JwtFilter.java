package com.project.AlexIad.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserDetailsServiceImplementation;
import com.project.AlexIad.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class JwtFilter extends AbstractAuthenticationProcessingFilter {
   private UserService userService;
   private UserDetailsServiceImplementation userDetailsServiceImplementation;
   private PasswordEncoder passwordEncoder;
   private User user;
   final String headerTitle = "Authorization";

    public JwtFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, UserDetailsServiceImplementation userDetailsServiceImplementation,
                     PasswordEncoder passwordEncoder, UserService userService) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        logger.info("...authentication");
          User userFromRequest = new ObjectMapper().readValue(httpServletRequest.getInputStream(),User.class);
        if(userFromRequest!=null){
           User userFromDB = userService.findUserByLogin(userFromRequest.getUsername());   // can use userDetailsServiceImplementation , but redundant , b return entity of UserDetail impl class User
            this.user = userFromDB;
            System.out.println(userFromDB + "   userFromDB!!!!!!!");
            if(userFromDB!=null){
                boolean isCorrectPassword = passwordEncoder.matches(userFromRequest.getPassword(), userFromDB.getPassword());
                if(isCorrectPassword){
                    Authentication authentication = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                            userFromRequest.getUsername(),userFromRequest.getPassword(),userFromDB.getAuthorities()));
                   return authentication;
                }
            }
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("Successful authentication!");
        Date date = Date.from(LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder()
                .setSubject(this.user.getUsername())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512,"adminKey")
                .compact();
        this.user.setToken(token);

        userService.saveCleanUser(this.user);
        response.addHeader(headerTitle,"Bearer " + token);
        chain.doFilter(request,response);
    }
}
