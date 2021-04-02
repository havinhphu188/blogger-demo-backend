package com.example.bloggerdemo.service.strategy.checkunique;

import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("displayName")
public class CheckIfDisplayNameUnique implements CheckUnique {
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public CheckIfDisplayNameUnique(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    @Override
    public boolean isUnique(String displayName) {
        return !this.bloggerUserRepository.existsByDisplayName(displayName);
    }
}
