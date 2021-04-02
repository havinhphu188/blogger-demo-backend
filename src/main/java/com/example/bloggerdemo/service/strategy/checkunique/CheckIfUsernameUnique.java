package com.example.bloggerdemo.service.strategy.checkunique;

import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("username")
public class CheckIfUsernameUnique implements CheckUnique {
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public CheckIfUsernameUnique(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    @Override
    public boolean isUnique(String username) {
        return !this.bloggerUserRepository.existsByUsername(username);
    }
}
