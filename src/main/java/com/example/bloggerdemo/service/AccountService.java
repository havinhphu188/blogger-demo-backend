package com.example.bloggerdemo.service;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public AccountService(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public void registerUser(BloggerUser bloggerUser) {
        this.bloggerUserRepository.save(bloggerUser);
    }

    public boolean isUsernameUnique(String username) {
        return !this.bloggerUserRepository.existsByUsername(username);
    }
}
