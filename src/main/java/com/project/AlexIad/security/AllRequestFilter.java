package com.project.AlexIad.security;

import com.project.AlexIad.models.User;
import com.project.AlexIad.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AllRequestFilter extends GenericFilterBean {
    private UserService userService;
    final String headerTitle = "Authorization";

    public AllRequestFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    logger.info("... do filter");
        Authentication authentication = null;
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String header = httpRequest.getHeader(headerTitle);
        if(header!=null){
            String clearToken = header.substring(7);
            User userFromDB = userService.findUserByToken(clearToken);
              authentication = new UsernamePasswordAuthenticationToken(userFromDB.getUsername(),null,userFromDB.getAuthorities());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
