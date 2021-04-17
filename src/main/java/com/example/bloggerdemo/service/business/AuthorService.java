package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Log4j2
public class AuthorService {
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public AuthorService(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public BloggerUser getAuthorInfo(int authorId) {
        log.debug("Getting author info");
        return bloggerUserRepository
                .findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + authorId + " not found"));
    }

    @Transactional
    public boolean subscribeOrUnsubscribeToAuthor(int authorId, int userId){
        log.debug("subscribing Or Unsubscribing To Author. authorId: {}, userId: {}", authorId,userId);
        boolean isUserFollowAuthor = this.bloggerUserRepository.isUserSubscribeToAuthor(authorId, userId);
        if (isUserFollowAuthor){
            this.bloggerUserRepository.unsubscribeToAuthor(authorId, userId);
            log.debug("unsubscribed to author. authorId: {}, userId: {}", authorId, userId);
        }else{
            this.bloggerUserRepository.subscribeToAuthor(authorId, userId);
            log.debug("subscribed to author. authorId: {}, userId: {}", authorId, userId);
        }
        return !isUserFollowAuthor;
    }

    public boolean isUserSubscribeToAuthor(int authorId, int userId) {
        log.debug("Getting isUserFollowAuthor. authorId: {}, userId: {}", authorId, userId);
        return this.bloggerUserRepository
                 .isUserSubscribeToAuthor(authorId, userId);
    }
}
