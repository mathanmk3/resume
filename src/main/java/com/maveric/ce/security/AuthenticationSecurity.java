package com.maveric.ce.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class AuthenticationSecurity {

    @Autowired
    AuthorizationSecurity authorizationSecurity;
    @Value("${superUserUrl}")
    String adminUrl;
    @Value("${customerUrl}")
    String customerUrl;
    @Value("${commonUrl}")
    String commonUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpServletRequest requestt,
                                                   HttpServletResponse response) throws Exception {
        final String[] CUSTOMER_WISHLIST=customerUrl.split(",");
        final String[] SUPER_WISHLIST=adminUrl.split(",");
        http.csrf(AbstractHttpConfigurer::disable).csrf(crf -> crf.disable())
                .authorizeHttpRequests(request -> request.requestMatchers(commonUrl).permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers(CUSTOMER_WISHLIST).hasRole("CUSTOMER"))
                .authorizeHttpRequests(request -> request.requestMatchers(SUPER_WISHLIST).hasRole("SUPERUSER"))
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationSecurity, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->exceptionHandling.accessDeniedHandler(new SerurityExceptionHandler()))
                .exceptionHandling(exceptionHandling ->exceptionHandling.authenticationEntryPoint(new NoResourceHandler()));
        return http.build();
    }


}
