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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    @Value("${crossOriginUrl}")
    String crossOriginUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpServletRequest requestt,
                                                   HttpServletResponse response) throws Exception {

        final String[] CUSTOMER_WISHLIST = customerUrl.split(",");
        final String[] SUPER_WISHLIST = adminUrl.split(",");
        http
                .cors(cors -> cors.configurationSource(corsOrginConfig()))
                .csrf(AbstractHttpConfigurer::disable).csrf(crf -> crf.disable())
                .authorizeHttpRequests(request -> request.requestMatchers(commonUrl).permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers(CUSTOMER_WISHLIST).hasRole("CUSTOMER"))
                .authorizeHttpRequests(request -> request.requestMatchers(SUPER_WISHLIST).hasRole("SUPERUSER"))
                .authorizeHttpRequests(request -> request.requestMatchers(AUTH_WISHLIST).permitAll())
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationSecurity, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(new SerurityExceptionHandler()))
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new NoResourceHandler()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsOrginConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(crossOriginUrl,commonUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }

    private static final String[]  AUTH_WISHLIST = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

}
