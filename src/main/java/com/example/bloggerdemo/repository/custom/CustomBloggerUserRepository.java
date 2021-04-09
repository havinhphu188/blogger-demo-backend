package com.example.bloggerdemo.repository.custom;

import com.example.bloggerdemo.model.BloggerUser;

import java.util.List;

public interface CustomBloggerUserRepository {
    void subscribeToAuthor(int authorId, int userId);

    void unsubscribeToAuthor(int authorId, int userId);

    boolean isUserSubscribeToAuthor(Integer authorId, Integer userId);

    List<BloggerUser> getListOfSubscribedAuthor(int userId);
}
