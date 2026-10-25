package org.BaseAPI.security.configs;

import org.BaseAPI.security.Services.AuthService;
import org.BaseAPI.security.Services.JwtAuthFilterService;
import org.BaseAPI.constants.SecurityRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

//    private final JwtTokenService jwtTokenService;
//


    @Autowired
    AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()


                        // Role-protected endpoints:
                        .requestMatchers("/user/**").permitAll()
                        //.requestMatchers("/user/add").permitAll()
                        .requestMatchers("/public/**", "/health", "/auth/login").permitAll()
                        //.requestMatchers("/user/**").hasAnyRole(SecurityRoles.ADMIN.name(), SecurityRoles.BASIC.name()) // requires ROLE_ADMIN or ROLE_BASIC

                        // Public endpoints:
                        // Everything else must be authenticated:
                        //.anyRequest().authenticated()

                )
                // Your JWT filter runs before UsernamePasswordAuthenticationFilter
                .addFilterBefore(new JwtAuthFilterService(authService),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
