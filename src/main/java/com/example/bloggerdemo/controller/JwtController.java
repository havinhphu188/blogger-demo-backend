package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.configuration.BloggerUserDetailsService;
import com.example.bloggerdemo.configuration.JwtRequest;
import com.example.bloggerdemo.configuration.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/authenticate")
public class JwtController {

    private final AuthenticationManager jwtAuthenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtController(AuthenticationManager jwtAuthenticationManager, UserDetailsService userDetailsService) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping()
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        jwtAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(userDetails.getPassword()));
    }
}
