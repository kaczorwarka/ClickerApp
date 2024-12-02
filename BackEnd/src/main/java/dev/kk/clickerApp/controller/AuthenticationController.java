package dev.kk.clickerApp.controller;

import dev.kk.clickerApp.auth.AuthenticationRequest;
import dev.kk.clickerApp.auth.AuthenticationResponse;
import dev.kk.clickerApp.auth.RegisterRequest;
import dev.kk.clickerApp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public AuthenticationResponse register(@RequestBody RegisterRequest registerRequest){
        try {
            return authenticationService.register(registerRequest);
        } catch (DuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public AuthenticationResponse authenticate (@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }
}
