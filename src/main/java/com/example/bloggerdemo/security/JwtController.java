package com.example.bloggerdemo.security;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/authenticate")
public class JwtController {

    private final AuthenticationManager jwtAuthenticationManager;

    private final JwtTokenUtil jwtTokenUtil;
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public JwtController(AuthenticationManager jwtAuthenticationManager, JwtTokenUtil jwtTokenUtil, BloggerUserRepository bloggerUserRepository) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.bloggerUserRepository = bloggerUserRepository;
    }

    @PostMapping()
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        jwtAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));
        BloggerUser bloggerUser = bloggerUserRepository.findOneByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(authenticationRequest.getUsername()));
        final String token = jwtTokenUtil.generateToken(bloggerUser.getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
