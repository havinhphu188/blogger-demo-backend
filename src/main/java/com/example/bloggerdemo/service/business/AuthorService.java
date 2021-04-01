package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AuthorService {
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public AuthorService(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public BloggerUser getAuthorInfo(int authorId) {
        return bloggerUserRepository
                .findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + authorId + " not found"));
    }

    @Transactional
    public boolean subscribeOrUnsubcribeToAuthor(int authorId, int userId){
        boolean isUserFollowAuthor = this.bloggerUserRepository.isUserSubscribeToAuthor(authorId, userId);
        if (isUserFollowAuthor){
            this.bloggerUserRepository.unsubscribeToAuthor(authorId, userId);
        }else{
            this.bloggerUserRepository.subscribeToAuthor(authorId, userId);
        }
        return !isUserFollowAuthor;
    }

    public boolean isUserSubscribeToAuthor(int authorId, int userId) {
         return this.bloggerUserRepository
                 .isUserSubscribeToAuthor(authorId, userId);
    }
}
