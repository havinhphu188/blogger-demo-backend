package com.example.bloggerdemo.repository.custom;

public interface CustomArticleRepository {
    void addUserReactionByArticleId(Integer articleId, Integer userId);
}
