package dev.kk.clickerApp.service;

import dev.kk.clickerApp.auth.AuthenticationRequest;
import dev.kk.clickerApp.auth.AuthenticationResponse;
import dev.kk.clickerApp.auth.RegisterRequest;
import dev.kk.clickerApp.model.User;
import dev.kk.clickerApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = User
                .builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .amountOfLives(3)
                .build();

    userRepository.insert(user);
    return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepository.findUserByEmail(authenticationRequest.getEmail()).orElseThrow();
        return new AuthenticationResponse(jwtService.generateToken(user));
    }
}
