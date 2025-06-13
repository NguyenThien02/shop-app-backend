package com.CIC.shop_app_backend.config;

import com.CIC.shop_app_backend.entity.Role;
import com.CIC.shop_app_backend.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),

                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/webjars/**"
                            ).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/roles/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/users/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(POST,
                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.SELLER)

                            .requestMatchers(GET,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/carts/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(GET,
                                    String.format("%s/carts/**", apiPrefix)).hasRole(Role.USER)

                            .requestMatchers(POST,
                                    String.format("%s/cart-item/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(GET,
                                    String.format("%s/cart-item/**", apiPrefix)).hasRole(Role.USER)


                            .requestMatchers(GET,
                                    String.format("%s/orders/by-user-id**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(GET,
                                    String.format("%s/orders/by-seller-id**", apiPrefix)).hasRole(Role.SELLER)
                            .requestMatchers(PUT,
                                    String.format("%s/orders/update-order-status**", apiPrefix)).hasRole(Role.SELLER)
                            .requestMatchers(POST,
                                    String.format("%s/orders**", apiPrefix)).hasRole(Role.USER)

                            .requestMatchers(GET,
                                    String.format("%s/order-details/**", apiPrefix)).permitAll()

                            .requestMatchers(POST,
                                    String.format("%s/vouchers/**", apiPrefix)).hasRole(Role.SELLER)
                            .requestMatchers(GET,
                                    String.format("%s/vouchers/**", apiPrefix)).permitAll()

                            .anyRequest().authenticated();
                });
        http.cors(cors -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            cors.configurationSource(source);
        });
        return http.build();
    }
}