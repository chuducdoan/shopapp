package com.product.shoppapp.configurations;

import com.product.shoppapp.filters.JwtTokenFilter;
import com.product.shoppapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String prefix;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request
                        -> request.requestMatchers(
                                String.format("%s/users/register", prefix),
                                String.format("%s/users/login", prefix))
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, String.format("%s/categories**", prefix)).hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.POST, String.format("%s/categories/**", prefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/categories/**", prefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/categories/**", prefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, String.format("%s/products**", prefix)).hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.POST, String.format("%s/products/**", prefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/products/**", prefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/products/**", prefix)).hasAnyRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.POST, String.format("%s/orders/**", prefix)).hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.GET, String.format("%s/orders/**", prefix)).hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/orders/**", prefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/orders/**", prefix)).hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.POST, String.format("%s/order_details/**", prefix)).hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.GET, String.format("%s/order_details/**", prefix)).hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/order_details/**", prefix)).hasRole(Role.ADMIN)
                        .anyRequest().authenticated()
                )
                .build();
    }
}
