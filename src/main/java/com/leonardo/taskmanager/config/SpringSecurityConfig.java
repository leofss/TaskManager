package com.leonardo.taskmanager.config;

import com.leonardo.taskmanager.jwt.JwtAuthenticationEntryPoint;
import com.leonardo.taskmanager.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {
    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-taskmanager.html", "/docs-taskmanager/**",
            "/v3/api-docs/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll()
                        .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(
                        jwtAuthtorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

                ).build();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthtorizationFilter(){
        return new JwtAuthorizationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
