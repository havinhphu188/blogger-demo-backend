package com.example.bloggerdemo.repository.custom;

public interface CustomArticleRepository {
    void addUserReactionByArticleId(Integer articleId, Integer userId);
    boolean isUserReacted(Integer articleId, Integer userId);
    void removeUserReaction(Integer articleId, Integer userId);
}
