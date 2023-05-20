package com.mindhub.homebanking.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
                .antMatchers( HttpMethod.POST, "/api/login" , "/api/clients", "/api/clients/current/cards/postnet", "/confirm-account").permitAll()
                .antMatchers("/index.html" , "/web/styles/index.css" , "/web/js/index.js" , "/web/pages/signup.html" , "/web/styles/signup.css" , "/web/js/signUp.js" , "/assets/**").permitAll()
                .antMatchers("/web/**" , "/api/clients/current/**" , "/api/loans").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.POST,"/api/clients/current/accounts" , "/api/clients/current/cards" , "/api/clients/current/transactions" , "/api/logout" , "/api/loans" , "/api/current/loans", "/api/clients/current/transactions/pdf").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers( HttpMethod.PUT,"/api/clients", "/api/clients/current/cards", "/api/clients/current/accounts").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/loans/manager").hasAnyAuthority("ADMIN")
                .antMatchers("/management/**" , "/rest/**" , "/h2-console" , "/api/clients" , "/api/clients/").hasAuthority("ADMIN")
                .anyRequest().denyAll();
        http.formLogin()
                .usernameParameter("emailAdress")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout().logoutUrl("/api/logout").deleteCookies( "JSESSIONID" );

        // turn off checking for CSRF tokens
        http.csrf().disable();
        // disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((request , response , exclusion ) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
       // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, authenticate) -> clearAuthenticationAttributes(req));
       // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exclusion ) -> res.sendError(HttpServletResponse.SC_BAD_REQUEST));
       // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}

// Element Collection.
// Gradle.
// Dependencias, Rest repositories.
// jdk compilado, lenguaje máquina.
// csrf.
// dto ( Refuerzo general, transferir datos ( Recibir y Transferir ) :v ).
// Repositorios.
// Abstrancción en las clases, donde ta.
// No dar papaya. No hacer lo que dice que Ivan. Llevar a la zona de comfort.

