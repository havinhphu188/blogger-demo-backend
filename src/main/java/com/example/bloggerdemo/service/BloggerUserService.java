package com.example.bloggerdemo.service;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class BloggerUserService {
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public BloggerUserService(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public BloggerUser getAuthorInfo(int authorId) {
        return bloggerUserRepository
                .findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + authorId + " not found"));
    }
}
