package com.example.bloggerdemo.repository.custom;

public interface CustomBloggerUserRepository {
    void subscribeToAuthor(int authorId, int userId);

    void unsubscribeToAuthor(int authorId, int userId);

    boolean isUserSubscribeToAuthor(Integer authorId, Integer userId);
}
