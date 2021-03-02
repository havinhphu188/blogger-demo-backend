package com.example.bloggerdemo.security;

import com.example.bloggerdemo.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final String header = "Authorization";

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (hasAuthorizationToken(request)) {
            setAuthorizedIfTokenValid(request);
        }
        chain.doFilter(request, response);
    }

    private void setAuthorizedIfTokenValid(HttpServletRequest request) {
        String jwtToken = request.getHeader(header).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }


    private boolean hasAuthorizationToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(header);
        if (authorizationHeader == null) return false;
        if (!authorizationHeader.startsWith("Bearer ")) return false;
        return true;
    }
}