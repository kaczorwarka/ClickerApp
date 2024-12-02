package dev.kk.clickerApp.config;

import dev.kk.clickerApp.service.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //wylączenie csrf (REST API z tego nie korzysta
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/api/user/auth")
                                .permitAll()
                                .anyRequest()
                                .authenticated() //Wszystke enedpointy poza publicznymi mają być objęte autoryzajcą
                ).sessionManagement(
                        httpSecuritySessionManagementConfigurer ->httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //działnie aplikacji w trybie bezstanowym, nie używa sesi do zarządzania stanem uzytkowika (typowe dla REST API gdzie autoryacja to toekny z rest api
                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //dodanie providera i mojego autentykacyjnego filtra

        return http.build();
    }
}
