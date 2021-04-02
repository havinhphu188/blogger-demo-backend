package com.example.bloggerdemo.service.strategy.checkunique;

import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    @Override
    public CheckUniqueStrategy getStrategyName() {
        return CheckUniqueStrategy.DISPLAY_NAME;
    }
}
