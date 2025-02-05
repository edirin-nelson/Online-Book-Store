package com.interswitch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class BeanConfig {

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> SecurityContextHolder.clearContext();
    }

}
