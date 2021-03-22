package com.example.bloggerdemo.repository.custom.impl;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.UserReaction;
import com.example.bloggerdemo.repository.custom.CustomArticleRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addUserReactionByArticleId(Integer articleId, Integer userId) {
        UserReaction userReaction = new UserReaction();
        Article article = entityManager.getReference(Article.class, articleId);
        BloggerUser bloggerUser = entityManager.getReference(BloggerUser.class, userId);
        userReaction.setArticle(article);
        userReaction.setBloggerUser(bloggerUser);
        entityManager.persist(userReaction);
    }
}
