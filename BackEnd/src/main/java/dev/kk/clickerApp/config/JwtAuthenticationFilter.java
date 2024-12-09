package dev.kk.clickerApp.config;

import dev.kk.clickerApp.model.User;
import dev.kk.clickerApp.service.JwtService;
import dev.kk.clickerApp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// W ramach tej klasy zaimplementowane zosała validatacja JWT, generowanie oraz pobierane danych użytkonika

@Component //Wymagane żeby powiedziec springowi że to bin coś ala controller albo service.
@RequiredArgsConstructor //tworzy konstruktor z dowolnego final atrybuty dla tej klasy
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; //Nie dodaje do kostruktora bo robi to RequiredArgsConstructor
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.getUserName(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userService.getUserByEmail(userEmail);
            UserDetails userDetails =this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ); //token autntykacyjny
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                ); //dodanie szczegółow -> requesta
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); //aktualizacja tokena
            }
            filterChain.doFilter(request,response); //przekazanie możliwości użycia kolejnego filra
        }
    }
}
