package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.request.AuthRequest;
import edu.unimagdalena.clinica.dto.request.PasswordResetRequestDTO;
import edu.unimagdalena.clinica.dto.request.SignUpRequest;
import edu.unimagdalena.clinica.dto.response.LoginResponseDTO;
import edu.unimagdalena.clinica.exception.ResourceNotFoundException;
import edu.unimagdalena.clinica.exception.alreadyexists.EmailAlreadyExistsException;
import edu.unimagdalena.clinica.exception.alreadyexists.UsernameAlreadyExistsException;
import edu.unimagdalena.clinica.model.Role;
import edu.unimagdalena.clinica.model.User;
import edu.unimagdalena.clinica.repository.UserRepository;
import edu.unimagdalena.clinica.security.jwt.JwtService;
import edu.unimagdalena.clinica.security.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            return ResponseEntity.ok(new LoginResponseDTO(token, userInfoService.getUserRoles(authRequest.username())));
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userInfoService.addUser(signUpRequest));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDTO request) {
        userInfoService.resetPassword(request);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

}
