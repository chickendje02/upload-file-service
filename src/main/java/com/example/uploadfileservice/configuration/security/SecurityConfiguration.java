package com.example.uploadfileservice.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    MyUserDetailService myUserDetailService;

    @Autowired
    private CustomAuthenticationProvider authProvider;

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(authProvider);
//        return authenticationManagerBuilder.build();
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/login**"); // login page
        RequestMatcher h2Matcher = new AntPathRequestMatcher("/h2-console/**"); // h2 memory
        RequestMatcher swagger = new AntPathRequestMatcher("/swagger-resources/**");
        RequestMatcher swaggerUi = new AntPathRequestMatcher("/swagger-ui.html");
        RequestMatcher swaggerConfiguration = new AntPathRequestMatcher("/configuration/ui");
        RequestMatcher swaggerSecurity = new AntPathRequestMatcher("/configuration/security");
        RequestMatcher swaggerWebJars = new AntPathRequestMatcher("/webjars/**");
        RequestMatcher swaggerApiDoc = new AntPathRequestMatcher("/v2/api-docs");

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(requestMatcher, h2Matcher, swagger, swaggerConfiguration, swaggerSecurity, swaggerUi, swaggerWebJars, swaggerApiDoc).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .csrf()
                .ignoringAntMatchers("/h2-console/**", "swagger**", "/configuration**","/v2/api-docs","/webjars/**" )
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .formLogin();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return myUserDetailService;
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
