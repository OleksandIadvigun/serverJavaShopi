package com.project.AlexIad.security;
/**
 *
 * @author Alex Iadvigun
 * @version 1.0
 */
import com.project.AlexIad.services.UserDetailsServiceImplementation;
import com.project.AlexIad.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
              //  .cors().disable()
               // .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/registration", "/auth", "/confirm/*","/checkloginAndEmail","/forgotPassword","/download/**").permitAll()
                .antMatchers( "/products/**","/shops/**", "/alarms/**", "/addImage","/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
              //  .antMatchers(HttpMethod.OPTIONS,"/product").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AllRequestFilter(userService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter("/auth", authenticationManager(), userDetImp, passwordEncoder, userService), UsernamePasswordAuthenticationFilter.class);
    }

    private UserDetailsServiceImplementation userDetImp;
    private UserService userService;
    private PasswordEncoder passwordEncoder;


}

