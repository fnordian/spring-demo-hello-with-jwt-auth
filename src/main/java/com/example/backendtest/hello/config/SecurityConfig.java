package com.example.backendtest.hello.config;

import com.example.backendtest.hello.authentication.CustomUserDetailsService;
import com.example.backendtest.hello.authentication.JwtAuthenticationEntryPoint;
import com.example.backendtest.hello.authentication.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String jwtSigningKey;

    protected SecurityConfig(@Value("${jwtSigningKey:}") String jwtSigningKey) {
        super();
        this.jwtSigningKey = jwtSigningKey;
    }

    private CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();


    private JwtAuthenticationEntryPoint unauthorizedHandler = new JwtAuthenticationEntryPoint();


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(new JwtAuthenticationFilter(jwtSigningKey), UsernamePasswordAuthenticationFilter.class);
    }
}
