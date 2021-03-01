package com.example.bloggerdemo.security;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private BloggerUserRepository bloggerUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BloggerUser bloggerUser = this.bloggerUserRepository.findOneByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        return User
                .withUsername(bloggerUser.getUsername())
                .password(bloggerUser.getPassword())
                .roles("USER")
                .build();
    }
}
