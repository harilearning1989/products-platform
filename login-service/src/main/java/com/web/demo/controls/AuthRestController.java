package com.web.demo.controls;

import com.web.demo.records.LoginRequest;
import com.web.demo.records.LoginResponse;
import com.web.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/hello")
    public String login() {
        return "hello";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            //String role = authentication.getAuthorities().iterator().next().getAuthority();
            Set<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            String token = JwtUtil.generateToken(request.username(), roles);

            return new LoginResponse(token);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid Username or Password");
        }
    }
}
