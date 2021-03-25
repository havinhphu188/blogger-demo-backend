package com.example.bloggerdemo.repository.custom;

import com.example.bloggerdemo.model.Article;

import java.util.List;

public interface CustomArticleRepository {
    void addUserReactionByArticleId(Integer articleId, Integer userId);
    boolean isUserReacted(Integer articleId, Integer userId);
    void removeUserReaction(Integer articleId, Integer userId);
    List<Article> getArticleBySubscription(Integer userId);
}
