package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.request.AuthRequest;
import edu.unimagdalena.clinica.dto.request.SignUpRequest;
import edu.unimagdalena.clinica.exception.alreadyexists.EmailAlreadyExistsException;
import edu.unimagdalena.clinica.exception.alreadyexists.UsernameAlreadyExistsException;
import edu.unimagdalena.clinica.security.jwt.JwtService;
import edu.unimagdalena.clinica.security.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserInfoService userInfoService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (authRequest.username(), authRequest.password()));
        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.username());
            return ResponseEntity.ok(token);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userInfoService.existsByUsername(signUpRequest.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
        if(userInfoService.existsByEmail(signUpRequest.email())) {
            throw new EmailAlreadyExistsException("Email is already taken");
        }

        SignUpRequest response =  userInfoService.addUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
