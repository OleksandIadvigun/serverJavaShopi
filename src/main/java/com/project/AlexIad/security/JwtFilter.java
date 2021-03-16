package com.project.AlexIad.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserDetailsServiceImplementation;
import com.project.AlexIad.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;


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
        System.out.println("USEr from request " + userFromRequest);
        if(userFromRequest!=null){
           User userFromDB = userService.findUserByLogin(userFromRequest.getUsername());   // can use userDetailsServiceImplementation , but redundant , b return entity of UserDetail impl class User
            this.user = userFromDB;
            System.out.println(userFromDB + "   userFromDB!!!!!!!");
            if(userFromDB!=null){
                boolean isCorrectPassword = passwordEncoder.matches(userFromRequest.getPassword(), userFromDB.getPassword());
                System.out.println(" answer from enkoder" + isCorrectPassword);
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
//        Gson gson = new Gson();
//        String s = gson.toJson(this.user);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(this.user));
        response.getWriter().flush();
        response.getWriter().close();
      //  chain.doFilter(request,response);                      // todo ? don t work with this
    }
}
