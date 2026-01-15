package com.example.auth.controller;

import com.example.auth.model.UserCredential;
import com.example.auth.repository.UserCredentialRepository;
import com.example.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserCredentialRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );


        UserCredential user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getRole() == null || request.getRole().isBlank()) {
            return ResponseEntity.badRequest().body("username/password/role required");
        }

        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("username already exists");
        }

        UserCredential user = new UserCredential();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()); // ex: ADMIN / USER

        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth service OK");
    }
}
